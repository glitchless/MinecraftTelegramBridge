package ru.glitchless.telegrambridge.utils;

import ru.glitchless.telegrambridge.TelegramBridgeMod;

public class TextUtils {
    public static String boldInText(String text, String textToBold) {
        return text.replaceFirst(textToBold, "*" + textToBold + "*");
    }

    public static String translate(String key) {
        final String text = TelegramBridgeMod.getLangFile().getProperty(key);
        return text != null ? text : "Unknown key: " + key;
    }
}
