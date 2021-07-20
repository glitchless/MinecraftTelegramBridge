package ru.glitchless.telegrambridge.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.handlers.IMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.model.MessageObject;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusCommand implements IMessageReceiver {
    final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

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
        final Date worldTime = new Date(server.getEntityWorld().getWorldTime());

        final String timesOfDay = server.getEntityWorld().isDaytime() ? "day" : "night";
        final String time = dateFormat.format(worldTime);
        final String toSendMessage = TelegramBridgeConfig.text.status_cmd
                .replace("${time}", time).replace("${timesofday}", timesOfDay);
        TelegramBridgeMod.getContext().sendMessage(chatId, toSendMessage);
        return true;
    }
}
