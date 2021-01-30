package ru.glitchless.telegrambridge.config;

import org.bukkit.configuration.file.FileConfiguration;
import ru.glitchless.telegrambridge.core.config.AbstractConfig;
import ru.glitchless.telegrambridge.core.config.ConfigPath;
import ru.glitchless.telegrambridge.core.config.ConfigWorkaround;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;

import java.util.List;

public class BukkitConfig implements AbstractConfig {
    private FileConfiguration config;

    private BukkitConfig(FileConfiguration config) {
        this.config = config;
    }

    public static void init(FileConfiguration config) {
        final BukkitConfig wrapper = new BukkitConfig(config);
        ConfigWorkaround.init(wrapper, TelegramBridgeConfig.class);
        ConfigWorkaround.onReload();
    }

    @Override
    public Object getValue(ConfigPath path) {
        return config.get(path.toString());
    }

    @Override
    public void setList(ConfigPath path, List<?> value, String... comment) {
        config.addDefault(path.toString(), value);
    }

    @Override
    public void setValue(ConfigPath path, Object value, String... comment) {
        config.addDefault(path.toString(), value);
    }
}
