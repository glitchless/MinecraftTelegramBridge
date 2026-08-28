package ru.glitchless.telegrambridge.handlers;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
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

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        final List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        for (ServerPlayer player : players) {
            player.sendMessage(new TextComponent(textMessage), player.getUUID());
        }
        return true;
    }
}
