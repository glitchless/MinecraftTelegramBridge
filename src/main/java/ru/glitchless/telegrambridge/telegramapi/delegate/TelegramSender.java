package ru.glitchless.telegrambridge.telegramapi.delegate;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.utils.HttpUtils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TelegramSender {
    private final Logger logger;
    private final String SEND_URL;
    private final Queue<Pair<String, String>> pendingMessage = new ConcurrentLinkedQueue<>();

    public TelegramSender(String baseUrl, Logger logger) {

        this.logger = logger;
        this.SEND_URL = baseUrl + "/sendMessage";
    }

    public void sendPendingMessages() {
        Pair<String, String> message = pendingMessage.poll();
        while (message != null) {
            sendMessageInternal(message.getKey(), message.getValue());
            message = pendingMessage.poll();
        }
    }

    private void sendMessageInternal(String chatId, String message) {
        List<AbstractMap.SimpleEntry<String, Object>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>("parse_mode", "Markdown"));
        params.add(new AbstractMap.SimpleEntry<>("chat_id", chatId));
        params.add(new AbstractMap.SimpleEntry<>("text", message));

        try {
            String response = HttpUtils.doPostRequest(SEND_URL, params, logger);
            if (TelegramBridgeConfig.verbose_logging) {
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
