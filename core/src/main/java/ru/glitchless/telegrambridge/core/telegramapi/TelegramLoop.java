package ru.glitchless.telegrambridge.core.telegramapi;

public class TelegramLoop {
    private final TelegramContext context;
    private final LoopWithExpRetry receiverLoop;
    private final LoopWithExpRetry senderLoop;

    public TelegramLoop(TelegramContext context) {
        this.context = context;
        receiverLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
            @Override
            public void run() throws Exception {
                context.getReceiver().checkUpdate();
            }
        });
        senderLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
            @Override
            public void run() throws Exception {
                context.getSender().sendPendingMessages();
            }
        });
    }

    public void start() {
        receiverLoop.start();
        senderLoop.start();
    }
}
