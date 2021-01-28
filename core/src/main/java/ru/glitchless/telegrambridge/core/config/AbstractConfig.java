package ru.glitchless.telegrambridge.core.config;

import java.util.List;

public interface AbstractConfig {
    /**
     * @param path in format root.subroot.name
     * @return value
     */
    Object getValue(ConfigPath path);

    void setList(ConfigPath path, List<?> value, String... comment);

    void setValue(ConfigPath path, Object value, String... comment);
}
