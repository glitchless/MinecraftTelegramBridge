package ru.glitchless.telegrambridge.telegramapi.delegate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.telegramapi.model.TelegramAnswerObject;
import ru.glitchless.telegrambridge.telegramapi.model.UpdateObject;
import ru.glitchless.telegrambridge.utils.HttpUtils;
import ru.glitchless.telegrambridge.utils.LoggerUtils;
import ru.glitchless.telegrambridge.utils.TextUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramReceiver {
    private final String UPDATE_URL;
    private final Gson gson = new Gson();
    private final Type updateType = new TypeToken<TelegramAnswerObject<List<UpdateObject>>>() {
    }.getType();
    private final Logger logger;
    private final TelegramContext context;
    private final Map<String, IMessageReceiver> receiverMap = new ConcurrentHashMap<String, IMessageReceiver>();
    private final List<IMessageReceiver> globalMessageReceivers = new ArrayList<>();

    public TelegramReceiver(TelegramContext context) {
        UPDATE_URL = context.getBaseUrl() + "/getUpdates?allowed_updates=[\"message\"]&offset=%s&timeout="
                + TelegramBridgeConfig.telegramConfig.telegram_long_pooling_timeout;
        this.logger = context.getLogger();
        this.context = context;
    }

    public void checkUpdate() throws Exception {
        String updateJson = HttpUtils.httpGet(String.format(UPDATE_URL, TelegramOffsetDataHelper.getOffset() + 1));
        LoggerUtils.logInfoInternal(logger, "Get from telegram update " + updateJson);
        TelegramAnswerObject<List<UpdateObject>> updates = gson.fromJson(updateJson, updateType);

        for (UpdateObject update : updates.getResult()) {
            TelegramOffsetDataHelper.setIfLargerOffset(update.getUpdateId());
            processUpdate(update);
        }
    }

    private void processUpdate(@Nullable UpdateObject updateObject) {
        if (updateObject == null) {
            return;
        }

        if (updateObject.getMessage() == null) {
            LoggerUtils.logInfoInternal(logger, "I received message without message");
            return;
        }

        boolean success = false;
        for (IMessageReceiver receivers : globalMessageReceivers) {
            success |= receivers.onTelegramObjectMessage(updateObject.getMessage());
        }

        if (success) {
            return;
        }

        final String chatId = String.valueOf(updateObject.getMessage().getChat().getId());
        IMessageReceiver receiver = receiverMap.get(chatId);
        if (receiver != null) {
            receiver.onTelegramObjectMessage(updateObject.getMessage());
            return;
        }

        final String answer = TextUtils.translate("telegrambridge.notfoundchat")
                .replace("${chatid}", String.valueOf(updateObject.getMessage().getChat().getId()));
        context.sendMessage(chatId, answer);
    }

    public void setListener(IMessageReceiver messageReceiver, @Nullable String chatid) {
        if (chatid != null) {
            receiverMap.put(chatid, messageReceiver);
            return;
        }
        globalMessageReceivers.add(messageReceiver);
    }
}
