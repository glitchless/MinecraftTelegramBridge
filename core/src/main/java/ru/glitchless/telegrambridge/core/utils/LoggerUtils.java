package ru.glitchless.telegrambridge.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;

public class LoggerUtils {
    public static void logInfoInternal(Logger logger, String log) {
        if (!TelegramBridgeConfig.verbose_logging) {
            return;
        }
        logger.log(Level.INFO, log);
    }
}
