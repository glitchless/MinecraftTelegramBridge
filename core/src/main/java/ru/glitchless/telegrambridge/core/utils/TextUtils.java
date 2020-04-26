package ru.glitchless.telegrambridge.core.utils;

import ru.glitchless.telegrambridge.core.BuildConfig;

public class TextUtils {
    public static final String MOD_VERSION = BuildConfig.MOD_VERSION;

    public static String boldInText(String text, String textToBold) {
        return text.replaceFirst(textToBold, "*" + textToBold + "*");
    }
}
