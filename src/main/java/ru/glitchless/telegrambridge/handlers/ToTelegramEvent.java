package ru.glitchless.telegrambridge.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.utils.TextUtils;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ToTelegramEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        final ITextComponent textComponent = player.getCombatTracker().getDeathMessage();
        final String deathmessage = TextUtils.boldInText(textComponent.getString(),
                player.getGameProfile().getName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.getSource().getTrueSource() instanceof PlayerEntity
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
