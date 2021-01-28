package ru.glitchless.telegrambridge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import ru.glitchless.telegrambridge.config.BukkitConfig;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramLoop;
import ru.glitchless.telegrambridge.handlers.PlayerList;
import ru.glitchless.telegrambridge.handlers.ToMinecraftResender;
import ru.glitchless.telegrambridge.handlers.ToTelegramEvent;

public class TelegramBridgePlugin extends JavaPlugin {
    private static Logger logger = LogManager.getLogger();

    private static TelegramContext context;
    private static TelegramLoop telegramLoop;
    private static ToTelegramEvent toTelegramEvent = new ToTelegramEvent();

    public static TelegramContext getContext() {
        return context;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        BukkitConfig.init(getConfig());
        getConfig().options().copyDefaults(true);
        saveConfig();

        context = new TelegramContext(logger);
        telegramLoop = new TelegramLoop(context);

        getServer().getPluginManager().registerEvents(toTelegramEvent, this);
        onServerStart();
    }

    private void onServerStart() {
        telegramLoop.start();
        if (!TelegramBridgeConfig.relay_level.server_start) {
            return;
        }
        context.addListener(new ToMinecraftResender());
        context.addListener(new PlayerList());
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_start);
    }

    private void onServerStop() {
        if (!TelegramBridgeConfig.relay_level.server_stop) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_stop);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onServerStop();
    }
}
