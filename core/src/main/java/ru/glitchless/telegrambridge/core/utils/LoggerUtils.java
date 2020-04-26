package ru.glitchless.telegrambridge.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.core.config.ConfigWrapper;

public class LoggerUtils {
    public static void logInfoInternal(Logger logger, String log, ConfigWrapper config) {
        if (!config.isVerboseLogging()) {
            return;
        }
        logger.log(Level.INFO, log);
    }
}
