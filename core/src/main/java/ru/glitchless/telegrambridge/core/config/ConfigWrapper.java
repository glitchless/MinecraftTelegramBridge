package ru.glitchless.telegrambridge.core.config;

import java.net.Proxy;

public interface ConfigWrapper {
    boolean isVerboseLogging();

    String getTelegramApiToken();

    Integer getTelegramLongPoolingTimeout();

    Proxy getProxy();
}
