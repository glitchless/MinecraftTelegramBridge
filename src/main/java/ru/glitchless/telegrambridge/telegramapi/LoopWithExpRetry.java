package ru.glitchless.telegrambridge.telegramapi;

import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoopWithExpRetry extends Thread {
    private final static Logger logger = Logger.getLogger(LoopWithExpRetry.class.getName());
    private final ThrowableRunnable loop;
    private int retryCounter = 0;

    public LoopWithExpRetry(ThrowableRunnable loop) {
        this.loop = loop;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                loop.run();
                retryCounter = 0;
            } catch (Exception ex) {
                double timeout = Math.exp(retryCounter);
                if (TelegramBridgeConfig.verbose_logging) {
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
