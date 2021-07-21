package ru.glitchless.telegrambridge.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.utils.TextUtils;

import javax.annotation.Nonnull;

public class ToTelegramEvent {
    public static void broadcastToChats(@Nonnull String message) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            TelegramBridgeMod.getContext().sendMessage(id, message);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.entityLiving instanceof EntityPlayerMP)) {
            return;
        }
        final EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
        final IChatComponent textComponent = event.source.func_151519_b(event.entityLiving);
        final String deathmessage = TextUtils.boldInText(textComponent.getUnformattedText(),
                player.getGameProfile().getName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.source.getEntity() instanceof EntityPlayer
                && TelegramBridgeConfig.relay_level.user_kill_by_user) {
            broadcastToChats(message);
            return;
        }

        if (TelegramBridgeConfig.relay_level.user_kill_by_other) {
            broadcastToChats(message);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_join) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_join
                .replace("${nickname}", event.player.getDisplayName());
        broadcastToChats(message);
    }

    @SubscribeEvent
    public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_leave) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_leave
                .replace("${nickname}", event.player.getDisplayName());
        broadcastToChats(message);
    }
}
