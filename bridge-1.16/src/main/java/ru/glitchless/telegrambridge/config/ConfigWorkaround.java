package ru.glitchless.telegrambridge.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ConfigWorkaround {
    public static final String CATEGORY_GENERAL = "general";
    private static ConfigWorkaround INSTANCE = null;
    private final Map<String, ForgeConfigSpec.ConfigValue<?>> specs = new HashMap<>();

    public ConfigWorkaround(final ForgeConfigSpec.Builder builder) {
        try {
            createConfig(builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        INSTANCE = this;
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);

        if (INSTANCE != null) {
            try {
                INSTANCE.invalidateConfig();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setIfNotNull(Field field, ForgeConfigSpec.ConfigValue<?> spec, Object obj, Supplier<Object> valuePredict) throws IllegalAccessException {
        if (spec == null) {
            return;
        }
        field.set(obj, valuePredict.get());
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
        if (INSTANCE != null) {
            try {
                INSTANCE.invalidateConfig();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void createConfig(ForgeConfigSpec.Builder commonBuilder) throws IllegalAccessException {
        Class<TelegramBridgeConfig> clazz = TelegramBridgeConfig.class;
        commonBuilder.comment("General settings").push(CATEGORY_GENERAL);

        for (Field field : clazz.getDeclaredFields()) {
            addFieldToConfig(CATEGORY_GENERAL, commonBuilder, field, null);
        }

        commonBuilder.pop();

    }

    private void addFieldToConfig(String path, ForgeConfigSpec.Builder builder, Field field, Object obj) throws IllegalAccessException {
        final String currentPath = path + "." + field.getName();
        builder.comment("");
        final Comment comment = field.getAnnotation(Comment.class);
        if (comment != null) {
            builder.comment(comment.value());
        }
        if (field.getType().isAssignableFrom(List.class)) {
            specs.put(currentPath, builder.defineList(field.getName(),
                    (List<?>) field.getType().cast(field.get(obj)), Objects::nonNull));
            //field.set(obj, spec.get());
        } else if (field.getType() == String.class) {
            specs.put(currentPath, builder.define(field.getName(), (String) field.get(obj)));
            //field.set(obj, spec.get().toString());
        } else if (field.getType() == boolean.class) {
            specs.put(currentPath, builder.define(field.getName(), (boolean) field.get(obj)));
            //field.set(obj, spec.get());
        } else if (field.getType().isEnum()) {
            List<String> comments = new ArrayList<>();
            comments.add("Valid values:");
            for (Object enumObj : field.getType().getEnumConstants()) {
                comments.add(((Enum<?>) enumObj).name());
            }
            builder.comment(comments.toArray(new String[]{}));

            Enum<?> enumField = (Enum<?>) field.getType().cast(field.get(obj));
            specs.put(currentPath, builder.define(field.getName(), enumField.name()));
            //field.set(obj, Enum.valueOf((Class<Enum>) field.getType(), spec.get().toString()));
        } else if (field.getType().isAssignableFrom(Number.class)) {
            specs.put(currentPath, builder.define(field.getName(), field.get(obj)));
        } else if (field.getType() == int.class) {
            specs.put(currentPath, builder.define(field.getName(), (int) field.get(obj)));
        } else {
            builder.push(field.getName());
            for (Field innerField : field.get(obj).getClass().getDeclaredFields()) {
                addFieldToConfig(currentPath, builder, innerField, field.get(obj));
            }
            builder.pop();
        }
    }

    public void invalidateConfig(String path, Field field, Object obj) throws IllegalAccessException {
        final String currentPath = path + "." + field.getName();
        ForgeConfigSpec.ConfigValue<?> spec = specs.get(currentPath);

        if (field.getType().isAssignableFrom(List.class)) {
            setIfNotNull(field, spec, obj, spec::get);
        } else if (field.getType() == String.class) {
            setIfNotNull(field, spec, obj, () -> spec.get().toString());
        } else if (field.getType() == boolean.class) {
            setIfNotNull(field, spec, obj, spec::get);
        } else if (field.getType().isEnum()) {
            setIfNotNull(field, spec, obj, () -> Enum.valueOf((Class<Enum>) field.getType(), spec.get().toString()));
        } else if (field.getType().isAssignableFrom(Number.class)) {
            setIfNotNull(field, spec, obj, spec::get);
        } else if (field.getType() == int.class) {
            setIfNotNull(field, spec, obj, spec::get);
        } else {
            for (Field innerField : field.get(obj).getClass().getDeclaredFields()) {
                invalidateConfig(currentPath, innerField, field.get(obj));
            }
        }
    }

    public void invalidateConfig() throws IllegalAccessException {
        Class<TelegramBridgeConfig> clazz = TelegramBridgeConfig.class;

        for (Field field : clazz.getDeclaredFields()) {
            invalidateConfig(CATEGORY_GENERAL, field, null);
        }
    }

}
