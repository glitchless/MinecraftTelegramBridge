package ru.glitchless.telegrambridge.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import ru.glitchless.telegrambridge.core.config.AbstractConfig;
import ru.glitchless.telegrambridge.core.config.ConfigPath;
import ru.glitchless.telegrambridge.core.config.ConfigWorkaround;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForgeConfig implements AbstractConfig {
    private final Map<ConfigPath, ForgeConfigSpec.ConfigValue<?>> specs = new HashMap<>();
    private final ForgeConfigSpec.Builder builder;

    private ForgeConfig(final ForgeConfigSpec.Builder builder) {
        this.builder = builder;
    }

    public static void initConfig(String modid) {
        final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        final AbstractConfig abstractConfig = new ForgeConfig(configBuilder);
        ConfigWorkaround.init(abstractConfig);
        final ForgeConfigSpec forgeConfigSpec = configBuilder.build();
        final Path configPath = FMLPaths.CONFIGDIR.get().resolve(modid + ".toml");

        final CommentedFileConfig configData = CommentedFileConfig.builder(configPath)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        forgeConfigSpec.setConfig(configData);
        ConfigWorkaround.onReload();
    }

    @Override
    public Object getValue(ConfigPath path) {
        final ForgeConfigSpec.ConfigValue<?> configValue = specs.get(path);
        if (configValue == null) {
            return null;
        }
        return configValue.get();
    }

    @Override
    public void setList(ConfigPath path, List<?> value) {
        int level = pushTo(path.getParent());
        final ForgeConfigSpec.ConfigValue<?> configValue = builder.define(path.getName(), value, Objects::nonNull);
        specs.put(path, configValue);
        pop(level);
    }

    @Override
    public void setComment(ConfigPath path, String... comment) {
        int level = pushTo(path.getParent());
        builder.push(path.getName());
        //TODO builder.comment(comment);
        builder.pop();
        pop(level);
    }

    @Override
    public void setValue(ConfigPath path, Object value) {
        int level = pushTo(path.getParent());
        final ForgeConfigSpec.ConfigValue<?> configValue = builder.define(path.getName(), value);
        specs.put(path, configValue);
        pop(level);
    }

    private int pushTo(@Nullable ConfigPath path) {
        if (path == null || path.getParent() == null) {
            return 0;
        }
        int level = pushTo(path.getParent());
        builder.push(path.getName());
        return level + 1;
    }

    private void pop(int level) {
        for (int i = 0; i < level; i++) {
            builder.pop();
        }
    }
}
