package ru.glitchless.telegrambridge.handlers;

import ru.glitchless.telegrambridge.telegramapi.model.MessageObject;

import javax.annotation.Nonnull;

/**
 * Use {@link BaseMessageReceiver} instead for filter empty or invalid message
 */
public interface IMessageReceiver {
    boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject);
}
