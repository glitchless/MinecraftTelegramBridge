package ru.glitchless.telegrambridge.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.glitchless.telegrambridge.TelegramBridgePlugin;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.utils.TextUtils;

import javax.annotation.Nonnull;

public class ToTelegramEvent implements Listener {
    public static void broadcastToChats(@Nonnull String message) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            TelegramBridgePlugin.getContext().sendMessage(id, message);
        }
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_TELEGRAM) {
            return; // ignore
        }

        final String message = event.getMessage();

        if (message == null || message.isEmpty()) {
            return;
        }

        final String textMessage = TelegramBridgeConfig.text.chatmessage_to_telegram.replace("${nickname}", event.getPlayer().getDisplayName())
                .replace("${message}", message);
        broadcastToChats(textMessage);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getDeathMessage() == null) {
            return;
        }
        final String deathmessage = TextUtils.boldInText(event.getDeathMessage(),
                event.getEntity().getDisplayName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.getEntity().getKiller() != null) {
            if (TelegramBridgeConfig.relay_level.user_kill_by_user) {
                broadcastToChats(message);
            }
            return;
        }

        if (TelegramBridgeConfig.relay_level.user_kill_by_other) {
            broadcastToChats(message);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_join) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_join
                .replace("${nickname}", event.getPlayer().getDisplayName());
        broadcastToChats(message);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_leave) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_leave
                .replace("${nickname}", event.getPlayer().getDisplayName());
        broadcastToChats(message);
    }
}
