package ru.glitchless.telegrambridge.config;

import ru.glitchless.telegrambridge.core.config.ConfigWrapper;

public class ConfigWrapperImpl implements ConfigWrapper {
    @Override
    public boolean isVerboseLogging() {
        return TelegramBridgeConfig.verbose_logging;
    }

    @Override
    public String getTelegramApiToken() {
        return TelegramBridgeConfig.telegram_config.api_token;
    }

    @Override
    public Integer getTelegramLongPoolingTimeout() {
        return TelegramBridgeConfig.telegram_config.telegram_long_pooling_timeout;
    }
}
