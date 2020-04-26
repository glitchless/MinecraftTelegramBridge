package ru.glitchless.telegrambridge.core.telegramapi;

import ru.glitchless.telegrambridge.core.config.ConfigWrapper;

public class TelegramLoop {
    private final TelegramContext context;
    private final LoopWithExpRetry receiverLoop;
    private final LoopWithExpRetry senderLoop;

    public TelegramLoop(TelegramContext context, ConfigWrapper config) {
        this.context = context;
        receiverLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
            @Override
            public void run() throws Exception {
                context.getReceiver().checkUpdate();
            }
        }, config);
        senderLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
            @Override
            public void run() throws Exception {
                context.getSender().sendPendingMessages();
            }
        }, config);
    }

    public void start() {
        receiverLoop.start();
        senderLoop.start();
    }
}
