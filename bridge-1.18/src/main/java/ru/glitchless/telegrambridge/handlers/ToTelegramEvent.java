package ru.glitchless.telegrambridge.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.utils.TextUtils;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ToTelegramEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof ServerPlayer)) {
            return;
        }
        final ServerPlayer player = (ServerPlayer) event.getEntityLiving();
        final Component textComponent = player.getCombatTracker().getDeathMessage();
        final String deathmessage = TextUtils.boldInText(textComponent.getString(),
                player.getGameProfile().getName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.getSource().getEntity() instanceof Player
                && TelegramBridgeConfig.relay_level.user_kill_by_user) {
            broadcastToChats(message);
            return;
        }

        if (TelegramBridgeConfig.relay_level.user_kill_by_other) {
            broadcastToChats(message);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_join) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_join
                .replace("${nickname}", event.getPlayer().getDisplayName().getString());
        broadcastToChats(message);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_leave) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_leave
                .replace("${nickname}", event.getPlayer().getDisplayName().getString());
        broadcastToChats(message);
    }

    public static void broadcastToChats(@Nonnull String message) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            TelegramBridgeMod.getContext().sendMessage(id, message);
        }
    }
}
