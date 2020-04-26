package ru.glitchless.telegrambridge.core.telegramapi;

import ru.glitchless.telegrambridge.core.config.ConfigWrapper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoopWithExpRetry extends Thread {
    private final static Logger logger = Logger.getLogger(LoopWithExpRetry.class.getName());
    private final ThrowableRunnable loop;
    private int retryCounter = 0;
    private ConfigWrapper config;

    public LoopWithExpRetry(ThrowableRunnable loop, ConfigWrapper config) {
        this.loop = loop;
        this.config = config;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                loop.run();
                retryCounter = 0;
            } catch (Exception ex) {
                double timeout = Math.exp(retryCounter);
                if (config.isVerboseLogging()) {
                    logger.log(Level.WARNING, "Failed retry " + retryCounter + ". Sleep: " + timeout, ex);
                    ex.printStackTrace();
                }
                if (timeout == Double.POSITIVE_INFINITY) {
                    retryCounter = 0;
                    continue;
                }
                retryCounter++;
                try {
                    Thread.sleep((long) timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static interface ThrowableRunnable {
        void run() throws Exception;
    }
}
