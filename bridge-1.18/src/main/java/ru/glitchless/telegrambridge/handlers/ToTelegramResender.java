package ru.glitchless.telegrambridge.handlers;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;

import static ru.glitchless.telegrambridge.handlers.ToTelegramEvent.broadcastToChats;

@Mod.EventBusSubscriber
public class ToTelegramResender {
    @SubscribeEvent
    public static void onChatMessage(ServerChatEvent event) {
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_TELEGRAM) {
            return; // ignore
        }

        final String message = event.getMessage();

        if (message == null || message.isEmpty()) {
            return;
        }

        final String textMessage = TelegramBridgeConfig.text.chatmessage_to_telegram.replace("${nickname}", event.getUsername()).replace("${message}", message);
        broadcastToChats(textMessage);
    }

}
