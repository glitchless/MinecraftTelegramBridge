package ru.glitchless.telegrambridge.config;

import ru.glitchless.telegrambridge.core.config.ConfigWrapper;

import java.net.InetSocketAddress;
import java.net.Proxy;

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

    @Override
    public Proxy getProxy() {
        if(TelegramBridgeConfig.proxy.proxy == Proxy.Type.DIRECT) {
            return Proxy.NO_PROXY;
        } else {
            return new Proxy(TelegramBridgeConfig.proxy.proxy, new InetSocketAddress(TelegramBridgeConfig.proxy.addr, TelegramBridgeConfig.proxy.port));
        }
    }
}
