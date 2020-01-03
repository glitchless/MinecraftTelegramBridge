package ru.glitchless.telegrambridge.telegramapi;

import org.apache.logging.log4j.Level;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;

public class TelegramLoop extends Thread {
    private final TelegramContext context;
    private int retryCounter = 0;

    public TelegramLoop(TelegramContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            context.getLogger().info("Run telegram loop");
            try {
                context.getReceiver().checkUpdate();
                context.getSender().sendPendingMessages();
                retryCounter = 0;
            } catch (Exception ex) {
                double timeout = Math.exp(retryCounter);
                if (TelegramBridgeConfig.verbose_logging) {
                    context.getLogger().log(Level.ERROR, "Failed retry " + retryCounter + ". Sleep: " + timeout, ex);
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
}
