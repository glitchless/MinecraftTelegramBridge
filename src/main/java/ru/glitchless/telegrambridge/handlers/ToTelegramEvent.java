package ru.glitchless.telegrambridge.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.utils.TextUtils;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ToTelegramEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) {
            return;
        }
        final EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
        final ITextComponent textComponent = player.getCombatTracker().getDeathMessage();
        final String deathmessage = TextUtils.boldInText(textComponent.getUnformattedText(), player.getGameProfile().getName());
        final String message = TelegramBridgeConfig.text.death_message.replace("${deathmessage}", deathmessage);
        if (event.getSource().getTrueSource() instanceof EntityPlayer
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
                .replace("${nickname}", event.player.getDisplayNameString());
        broadcastToChats(message);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!TelegramBridgeConfig.relay_level.user_leave) {
            return;
        }
        final String message = TelegramBridgeConfig.text.player_leave
                .replace("${nickname}", event.player.getDisplayNameString());
        broadcastToChats(message);
    }

    public static void broadcastToChats(@Nonnull String message) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            TelegramBridgeMod.getContext().sendMessage(id, message);
        }
    }
}
