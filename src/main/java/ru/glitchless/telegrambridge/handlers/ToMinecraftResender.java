package ru.glitchless.telegrambridge.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.telegramapi.model.MessageObject;
import ru.glitchless.telegrambridge.telegramapi.model.UserObject;

import javax.annotation.Nonnull;

public class ToMinecraftResender extends BaseMessageReceiver {
    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        final String chatId = String.valueOf(messageObject.getChat().getId());

        if (!findChatId(chatId)) {
            return false;
        }
        return super.onTelegramObjectMessage(messageObject);
    }

    @Override
    public boolean onTelegramMessage(UserObject userObject, @Nonnull String message) {
        if (isCommand(message)) {
            return false;
        }
        if (TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TWO_SIDE
                && TelegramBridgeConfig.relay_mode != TelegramBridgeConfig.RelayMode.TO_MINECRAFT) {
            return true; // ignore
        }

        String textMessage = TelegramBridgeConfig.text.chatmessage_to_minecraft.replace("${nickname}", userObject.getUsername()).replace("${message}", message);

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        server.getPlayerList().sendMessage(new StringTextComponent(textMessage));
        return true;
    }

    private boolean findChatId(String chatId) {
        for (String id : TelegramBridgeConfig.chat_ids) {
            if (id.equals(chatId)) {
                return true;
            }
        }
        return false;
    }

}
