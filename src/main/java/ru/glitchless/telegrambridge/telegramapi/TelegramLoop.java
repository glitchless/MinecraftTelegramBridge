package ru.glitchless.telegrambridge.telegramapi;

public class TelegramLoop {
    private final TelegramContext context;
    private LoopWithExpRetry receiverLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
        @Override
        public void run() throws Exception {
            context.getReceiver().checkUpdate();
        }
    });
    private LoopWithExpRetry senderLoop = new LoopWithExpRetry(new LoopWithExpRetry.ThrowableRunnable() {
        @Override
        public void run() throws Exception {
            context.getSender().sendPendingMessages();
        }
    });

    public TelegramLoop(TelegramContext context) {
        this.context = context;
    }

    public void start() {
        receiverLoop.start();
        senderLoop.start();
    }
}
