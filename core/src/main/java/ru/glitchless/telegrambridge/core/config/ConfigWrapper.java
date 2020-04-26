package ru.glitchless.telegrambridge.core.config;

public interface ConfigWrapper {
    boolean isVerboseLogging();

    String getTelegramApiToken();

    Integer getTelegramLongPoolingTimeout();
}
