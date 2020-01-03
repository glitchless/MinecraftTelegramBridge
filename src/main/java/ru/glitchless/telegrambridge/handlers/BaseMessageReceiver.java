package ru.glitchless.telegrambridge.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.telegramapi.model.MessageObject;
import ru.glitchless.telegrambridge.telegramapi.model.UserObject;
import ru.glitchless.telegrambridge.utils.LoggerUtils;

import javax.annotation.Nonnull;


public abstract class BaseMessageReceiver implements IMessageReceiver {
    private final Logger logger = LogManager.getLogger(BaseMessageReceiver.class);

    /**
     * Use this
     */
    protected boolean onTelegramMessage(UserObject userObject, @Nonnull String message) {
        return false;
    }

    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            LoggerUtils.logInfoInternal(logger, "I received message without text");
            return false;
        }
        return onTelegramMessage(messageObject.getFrom(), messageText);
    }
}
