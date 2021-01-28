package ru.glitchless.telegrambridge.core.telegramapi;

import java.util.concurrent.atomic.AtomicBoolean;

public class TelegramLoop {
    private final TelegramContext context;
    private final LoopWithExpRetry receiverLoop;
    private final LoopWithExpRetry senderLoop;
    private final AtomicBoolean started = new AtomicBoolean(false);

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
        if (!started.getAndSet(true)) {
            receiverLoop.start();
            senderLoop.start();
        }
    }
}
