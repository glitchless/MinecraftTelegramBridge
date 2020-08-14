package ru.glitchless.telegrambridge.core.telegramapi.delegate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.config.ConfigWrapper;
import ru.glitchless.telegrambridge.core.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.core.telegramapi.model.TelegramAnswerObject;
import ru.glitchless.telegrambridge.core.telegramapi.model.UpdateObject;
import ru.glitchless.telegrambridge.core.utils.HttpUtils;
import ru.glitchless.telegrambridge.core.utils.LoggerUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TelegramReceiver {
    private final String UPDATE_URL;
    private final Gson gson = new Gson();
    private final Type updateType = new TypeToken<TelegramAnswerObject<List<UpdateObject>>>() {
    }.getType();
    private final Logger logger;
    private final TelegramContext context;
    private final ConfigWrapper config;
    private final List<IMessageReceiver> receivers = new ArrayList<>();

    public TelegramReceiver(TelegramContext context, ConfigWrapper config) {
        this.config = config;
        this.logger = context.getLogger();
        this.context = context;
        UPDATE_URL = context.getBaseUrl() + "/getUpdates?allowed_updates=[\"message\"]&offset=%s&timeout="
                + config.getTelegramLongPoolingTimeout();

    }

    public void checkUpdate() throws Exception {
        String updateJson = HttpUtils.httpGet(String.format(UPDATE_URL, TelegramOffsetDataHelper.getOffset() + 1), config.getProxy());
        LoggerUtils.logInfoInternal(logger, "Get from telegram update " + updateJson, config);
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
            LoggerUtils.logInfoInternal(logger, "I received message without message", config);
            return;
        }

        boolean success = false;
        for (IMessageReceiver receivers : receivers) {
            try {
                success |= receivers.onTelegramObjectMessage(updateObject.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (success) {
            return;
        }
    }

    public void addListener(IMessageReceiver messageReceiver) {
        receivers.add(messageReceiver);
    }
}
