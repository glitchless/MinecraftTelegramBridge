package ru.glitchless.telegrambridge.core.utils;

import javax.annotation.Nullable;

public class ArrayUtils {
    public static boolean isNullOrContainsNull(@Nullable Object[] objs) {
        if (objs == null) {
            return true;
        }
        if (objs.length == 0) {
            return false;
        }
        for (Object obj : objs) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }
}
