package ru.glitchless.telegrambridge.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
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
            TelegramBridgeMod.getContext().sendMessage(chatId, "Никого онлайн. Может, пора это исправить? :)");
            return true;
        }

        StringBuilder sb = new StringBuilder("*Игроки онлайн*\n\n");
        for (int i = 0; i < players.size(); i++) {
            sb.append(i + 1).append(". ").append(players.get(i).replace("_", "\\_")).append('\n');
        }

        sb.append("\nВсего игроков: *").append(players.size()).append('*');
        TelegramBridgeMod.getContext().sendMessage(chatId, sb.toString());
        return true;
    }

    private List<String> getPlayerList() {
        final MinecraftServer is = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (is == null) {
            return Collections.emptyList();
        }
        final List<String> playerList = new ArrayList<String>();
        for (EntityPlayerMP player : is.getPlayerList().getPlayers()) {
            playerList.add(player.getGameProfile().getName());
        }

        return playerList;
    }
}
