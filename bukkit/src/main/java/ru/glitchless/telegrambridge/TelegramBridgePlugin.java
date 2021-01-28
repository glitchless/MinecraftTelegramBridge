package ru.glitchless.telegrambridge;

import org.bukkit.plugin.java.JavaPlugin;

public class TelegramBridgePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getConsoleSender().sendMessage("Test");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getServer().getConsoleSender().sendMessage("Test");
    }
}
