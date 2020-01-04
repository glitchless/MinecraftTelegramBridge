package ru.glitchless.telegrambridge.handlers;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.utils.TextUtils;

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

        final String textMessage = TextUtils.translate("telegrambridge.telegramchatmessage").replace("${nickname}", event.getUsername()).replace("${message}", message);
        broadcastToChats(textMessage);
    }

}
