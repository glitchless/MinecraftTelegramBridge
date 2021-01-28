package ru.glitchless.telegrambridge.core.config;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigWorkaround {
    public static final ConfigPath CATEGORY_GENERAL = new ConfigPath("general");
    private static Map<String, Object> specs = new HashMap<>();
    private static AbstractConfig abstractConfig;

    public static void init(AbstractConfig clientConfig) {
        try {
            abstractConfig = clientConfig;
            createConfig();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void onReload() {
        try {
            invalidateConfig();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void invalidateConfig() throws IllegalAccessException {
        Class<TelegramBridgeConfig> clazz = TelegramBridgeConfig.class;

        for (Field field : clazz.getDeclaredFields()) {
            invalidateConfig(CATEGORY_GENERAL, field, null);
        }
    }

    private static void setIfNotNull(Field field, Object objForSet, Object value) throws IllegalAccessException {
        if (value == null) {
            return;
        }
        field.set(objForSet, value);
    }

    private static void createConfig() throws IllegalAccessException {
        Class<TelegramBridgeConfig> clazz = TelegramBridgeConfig.class;

        for (Field field : clazz.getDeclaredFields()) {
            addFieldToConfig(CATEGORY_GENERAL, field, null);
        }
    }

    private static void addFieldToConfig(ConfigPath path, Field field, Object obj) throws IllegalAccessException {
        final ConfigPath currentPath = new ConfigPath(path, field.getName());
        //abstractConfig.setComment(path, "");
        final Comment commentAnnotation = field.getAnnotation(Comment.class);
        String comment = null;
        if (commentAnnotation != null) {
            comment = commentAnnotation.value();
        }
        if (field.getType().isAssignableFrom(List.class)) {
            abstractConfig.setList(currentPath,
                    (List<?>) field.getType().cast(field.get(obj)), comment);
            //field.set(obj, spec.get());
        } else if (field.getType() == String.class) {
            abstractConfig.setValue(currentPath, field.get(obj), comment);
            //field.set(obj, spec.get().toString());
        } else if (field.getType() == boolean.class) {
            abstractConfig.setValue(currentPath, field.get(obj), comment);
            //field.set(obj, spec.get());
        } else if (field.getType().isEnum()) {
            List<String> comments = new ArrayList<>();
            comments.add("Valid values:");
            for (Object enumObj : field.getType().getEnumConstants()) {
                comments.add(((Enum<?>) enumObj).name());
            }

            Enum<?> enumField = (Enum<?>) field.getType().cast(field.get(obj));
            abstractConfig.setValue(currentPath, enumField.name(), comments.toArray(new String[]{}));
            //field.set(obj, Enum.valueOf((Class<Enum>) field.getType(), spec.get().toString()));
        } else if (field.getType().isAssignableFrom(Number.class)) {
            abstractConfig.setValue(currentPath, field.get(obj));
        } else if (field.getType() == int.class) {
            abstractConfig.setValue(currentPath, field.get(obj));
        } else {
            for (Field innerField : field.get(obj).getClass().getDeclaredFields()) {
                addFieldToConfig(currentPath, innerField, field.get(obj));
            }
        }
    }

    /**
     * @param path  to config without name
     * @param field class field what need change
     * @param obj   config object (where contains field)
     * @throws IllegalAccessException
     */
    private static void invalidateConfig(ConfigPath path, Field field, Object obj) throws IllegalAccessException {
        final ConfigPath currentPath = new ConfigPath(path, field.getName());
        final Object configValue = abstractConfig.getValue(currentPath);

        if (configValue == null) {
            return;
        }

        if (field.getType().isAssignableFrom(List.class)) {
            setIfNotNull(field, obj, configValue);
        } else if (field.getType() == String.class) {
            setIfNotNull(field, obj, configValue.toString());
        } else if (field.getType() == boolean.class) {
            setIfNotNull(field, obj, configValue);
        } else if (field.getType().isEnum()) {
            setIfNotNull(field, obj, Enum.valueOf((Class<Enum>) field.getType(), configValue.toString()));
        } else if (field.getType().isAssignableFrom(Number.class)) {
            setIfNotNull(field, obj, configValue);
        } else if (field.getType() == int.class) {
            setIfNotNull(field, obj, configValue);
        } else {
            for (Field innerField : field.get(obj).getClass().getDeclaredFields()) {
                invalidateConfig(currentPath, innerField, field.get(obj));
            }
        }
    }

}
