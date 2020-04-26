package ru.glitchless.telegrambridge.core.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.telegramapi.model.MessageObject;
import ru.glitchless.telegrambridge.core.telegramapi.model.UserObject;

import javax.annotation.Nonnull;


public abstract class BaseMessageReceiver implements IMessageReceiver {
    private final Logger logger = LogManager.getLogger(BaseMessageReceiver.class);

    public static boolean isCommand(String message) {
        return message.startsWith("/");
    }

    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            logger.info("I received message without text");
            return false;
        }
        return onTelegramMessage(messageObject.getFrom(), messageText);
    }

    public abstract boolean onTelegramMessage(UserObject userObject, @Nonnull String message);
}
