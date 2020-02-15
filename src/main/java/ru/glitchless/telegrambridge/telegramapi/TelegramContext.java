package ru.glitchless.telegrambridge.telegramapi;

import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.telegramapi.delegate.TelegramReceiver;
import ru.glitchless.telegrambridge.telegramapi.delegate.TelegramSender;

public class TelegramContext {
    private final Logger logger;
    private final TelegramReceiver receiver;
    private final TelegramSender sender;

    public TelegramContext(Logger logger) {
        this.logger = logger;
        this.receiver = new TelegramReceiver(this);
        this.sender = new TelegramSender(this);
    }


    /**
     * Async send message
     *
     * @param chatId  telegram chat id
     * @param message telegram text. Support Markdown
     */
    public void sendMessage(String chatId, String message) {
        sender.sendMessage(chatId, message);
    }

    /**
     * Add listener for input message.
     *
     * @param messageReceiver listener
     */
    public void addListener(IMessageReceiver messageReceiver) {
        receiver.addListener(messageReceiver);
    }


    public String getBaseUrl() {
        return "https://api.telegram.org/bot" + TelegramBridgeConfig.telegram_config.api_token;
    }

    public Logger getLogger() {
        return logger;
    }

    protected TelegramReceiver getReceiver() {
        return receiver;
    }

    protected TelegramSender getSender() {
        return sender;
    }
}
