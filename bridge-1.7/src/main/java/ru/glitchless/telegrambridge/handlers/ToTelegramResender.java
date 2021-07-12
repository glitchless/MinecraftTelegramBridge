package ru.glitchless.telegrambridge.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;

import static ru.glitchless.telegrambridge.handlers.ToTelegramEvent.broadcastToChats;

public class ToTelegramResender {
    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_TELEGRAM) {
            return; // ignore
        }

        final String message = event.message;

        if (message == null || message.isEmpty()) {
            return;
        }

        final String textMessage = TelegramBridgeConfig.text.chatmessage_to_telegram.replace("${nickname}", event.username).replace("${message}", message);
        broadcastToChats(textMessage);
    }
}
