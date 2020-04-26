package ru.glitchless.telegrambridge.handlers;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.glitchless.telegrambridge.TelegramBridgeMod;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.handlers.BaseMessageReceiver;
import ru.glitchless.telegrambridge.core.telegramapi.model.MessageObject;
import ru.glitchless.telegrambridge.core.telegramapi.model.UserObject;

import javax.annotation.Nonnull;

public class ToMinecraftResender extends BaseMessageReceiver {
    @Override
    public boolean onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        final String chatId = String.valueOf(messageObject.getChat().getId());

        if (!findChatId(chatId)) {
            final String answer = TelegramBridgeConfig.text.notfoundchat
                    .replace("${chatid}", String.valueOf(messageObject.getChat().getId()));
            TelegramBridgeMod.getContext().sendMessage(chatId, answer);
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

        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(textMessage));
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
