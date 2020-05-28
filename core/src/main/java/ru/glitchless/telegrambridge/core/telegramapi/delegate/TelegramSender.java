package ru.glitchless.telegrambridge.core.telegramapi.delegate;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.config.ConfigWrapper;
import ru.glitchless.telegrambridge.core.utils.HttpUtils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TelegramSender {
    private final Logger logger;
    private final String SEND_URL;
    private final ConfigWrapper config;
    private final BlockingQueue<Pair<String, String>> pendingMessage = new LinkedBlockingQueue<>();

    public TelegramSender(String baseUrl, Logger logger, ConfigWrapper config) {
        this.logger = logger;
        this.SEND_URL = baseUrl + "/sendMessage";
        this.config = config;
    }

    public void sendPendingMessages() throws InterruptedException {
        Pair<String, String> message = pendingMessage.take();
        while (message != null) {
            sendMessageInternal(message.getKey(), message.getValue());
            message = pendingMessage.take();
        }
    }

    private void sendMessageInternal(String chatId, String message) {
        List<AbstractMap.SimpleEntry<String, Object>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>("parse_mode", "Markdown"));
        params.add(new AbstractMap.SimpleEntry<>("chat_id", chatId));
        params.add(new AbstractMap.SimpleEntry<>("text", message));

        try {
            String response = HttpUtils.doPostRequest(SEND_URL, params, logger);
            if (config.isVerboseLogging()) {
                logger.info("Telegram answer >> " + response);
            }
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String message) {
        pendingMessage.add(Pair.of(chatId, message));
    }
}
