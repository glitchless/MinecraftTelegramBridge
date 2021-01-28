package ru.glitchless.telegrambridge.core.telegramapi;

import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.delegate.TelegramReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.delegate.TelegramSender;

public class TelegramContext {
    private final String BASE_URL;
    private final Logger logger;
    private final TelegramReceiver receiver;
    private final TelegramSender sender;

    public TelegramContext(Logger logger) {
        this.logger = logger;
        this.BASE_URL = "https://api.telegram.org/bot" + TelegramBridgeConfig.telegram_config.api_token;
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
     * Add listener for input message.
     *
     * @param messageReceiver listener
     */
    public void addListener(IMessageReceiver messageReceiver) {
        receiver.addListener(messageReceiver);
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
