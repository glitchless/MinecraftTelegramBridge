package ru.glitchless.telegrambridge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ru.glitchless.telegrambridge.config.JSONConfig;
import ru.glitchless.telegrambridge.core.config.ConfigWorkaround;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;

import java.io.File;

@Mod(
        modid = TelegramBridgeMod.MODID,
        name = "Telegram Bridge",
        version = "${version}"
)
public class TelegramBridgeMod {
    public static final String MODID = "telegrambridge";
    private JSONConfig config;

    public TelegramBridgeMod() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        final File configFile = new File(event.getModConfigurationDirectory(), "telegrambridge.json");
        config = new JSONConfig(configFile);
        ConfigWorkaround.init(config, TelegramBridgeConfig.class);
        config.save();
    }
}
