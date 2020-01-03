package ru.glitchless.telegrambridge.telegramapi;

import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.telegramapi.delegate.TelegramReceiver;
import ru.glitchless.telegrambridge.telegramapi.delegate.TelegramSender;

import javax.annotation.Nullable;

public class TelegramContext {
    private final String BASE_URL = "https://api.telegram.org/bot" + TelegramBridgeConfig.telegramConfig.api_token;
    private final Logger logger;
    private final TelegramReceiver receiver;
    private final TelegramSender sender;

    public TelegramContext(Logger logger) {
        this.logger = logger;
        this.receiver = new TelegramReceiver(this);
        this.sender = new TelegramSender(BASE_URL, logger);
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
     * Set listener for input message.
     *
     * @param messageReceiver listener
     * @param chatId          null if is a global receiver. For command or other. Notnull for chat bridge
     */
    public void setListener(IMessageReceiver messageReceiver, @Nullable String chatId) {
        receiver.setListener(messageReceiver, chatId);
    }


    public String getBaseUrl() {
        return BASE_URL;
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
