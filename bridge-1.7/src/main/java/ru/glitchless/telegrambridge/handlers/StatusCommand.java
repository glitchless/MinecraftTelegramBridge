package ru.glitchless.telegrambridge.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.model.MessageObject;

import javax.annotation.Nonnull;

public class StatusCommand implements IMessageReceiver {
    private static final double TICKS_IN_DAY = 24000;
    private static final double HOURS_IN_DAY = 24;
    private static final double TICKS_IN_HOUR = TICKS_IN_DAY / HOURS_IN_DAY;
    private static final double MINUTES_IN_HOUR = 60;
    private static final double TICKS_IN_MINUTES = TICKS_IN_HOUR / MINUTES_IN_HOUR;
    private static final int HOUR_OFFSET = 6;

    public static String getMinecraftDayTime(long worldTime) {
        final long worldTimeNormalize = (long) (worldTime % TICKS_IN_DAY); // Sometimes worldTime can be great than 24000
        final long hourAbsolute = (long) (worldTimeNormalize / TICKS_IN_HOUR);
        final long hourRelative = (long) ((hourAbsolute + HOUR_OFFSET) % HOURS_IN_DAY);
        final long minutesAbsolute = (long) (worldTimeNormalize / TICKS_IN_MINUTES);
        final long minutesRelative = (long) (minutesAbsolute % MINUTES_IN_HOUR);
        final String time = String.format("%02d", hourRelative) + ":" + String.format("%02d", minutesRelative);
        return time;
    }

    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        final String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            return false;
        }

        if (!messageText.startsWith("/status")) {
            return false;
        }

        final String chatId = messageObject.getChat().getId().toString();

        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        final String timesOfDay = server.getEntityWorld().isDaytime() ? "day" : "night";
        final String time = getMinecraftDayTime(server.getEntityWorld().getWorldTime());
        final String toSendMessage = TelegramBridgeConfig.text.status_cmd
                .replace("${time}", time).replace("${timesofday}", timesOfDay);
        TelegramBridgeMod.getContext().sendMessage(chatId, toSendMessage);
        return true;
    }
}
