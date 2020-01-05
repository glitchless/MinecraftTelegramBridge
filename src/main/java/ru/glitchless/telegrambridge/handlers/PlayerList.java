package ru.glitchless.telegrambridge.handlers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.telegramapi.model.MessageObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerList implements IMessageReceiver {
    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            return false;
        }

        if (!messageText.startsWith("/players")) {
            return false;
        }

        String chatId = messageObject.getChat().getId().toString();
        final List<String> players = getPlayerList();
        if (players.isEmpty()) {
            TelegramBridgeMod.getContext().sendMessage(chatId, TelegramBridgeConfig.text.player_empty);
            return true;
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            sb.append(i + 1).append(". ").append(players.get(i).replace("_", "\\_")).append('\n');
        }
        final String message = TelegramBridgeConfig.text.player_list
                .replace("${endline}", "\n").replace("${playerlist}", sb.toString())
                .replace("${playercount}", String.valueOf(players.size()));
        TelegramBridgeMod.getContext().sendMessage(chatId, message);
        return true;
    }

    private List<String> getPlayerList() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return Collections.emptyList();
        }
        final List<String> playerList = new ArrayList<String>();
        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            playerList.add(player.getGameProfile().getName());
        }

        return playerList;
    }
}
