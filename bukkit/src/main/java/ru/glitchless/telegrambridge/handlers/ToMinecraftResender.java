package ru.glitchless.telegrambridge.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.handlers.BaseMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.model.MessageObject;
import ru.glitchless.telegrambridge.core.telegramapi.model.UserObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ToMinecraftResender extends BaseMessageReceiver {
    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        final String chatId = String.valueOf(messageObject.getChat().getId());

        if (!findChatId(chatId)) {
            return false;
        }
        return super.onTelegramObjectMessage(messageObject);
    }

    @Override
    public boolean onTelegramMessage(UserObject userObject, @Nonnull String message) {
        if (isCommand(message)) {
            return false;
        }
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_MINECRAFT) {
            return true; // ignore
        }

        String textMessage = TelegramBridgeConfig.text.chatmessage_to_minecraft.replace("${nickname}", userObject.getUsername()).replace("${message}", message);

        final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            player.sendMessage(textMessage);
        }
        return true;
    }
}
