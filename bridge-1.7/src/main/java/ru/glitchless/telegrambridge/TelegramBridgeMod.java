package ru.glitchless.telegrambridge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.JSONConfig;
import ru.glitchless.telegrambridge.core.config.ConfigWorkaround;
import ru.glitchless.telegrambridge.core.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramLoop;
import ru.glitchless.telegrambridge.handlers.PlayerList;
import ru.glitchless.telegrambridge.handlers.ToMinecraftResender;
import ru.glitchless.telegrambridge.handlers.ToTelegramEvent;
import ru.glitchless.telegrambridge.handlers.ToTelegramResender;

import java.io.File;

@Mod(
        modid = TelegramBridgeMod.MODID,
        name = "Telegram Bridge",
        version = "${version}",
        acceptableRemoteVersions = "*"
)
public class TelegramBridgeMod {
    public static final String MODID = "telegrambridge";

    private static Logger logger;
    private static TelegramContext context;
    private static TelegramLoop telegramLoop;

    public TelegramBridgeMod() {
        FMLCommonHandler.instance().bus().register(new ToTelegramEvent());
        MinecraftForge.EVENT_BUS.register(new ToTelegramResender());
    }

    public static TelegramContext getContext() {
        return context;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        final File configFile = new File(event.getModConfigurationDirectory(), "telegrambridge.json");
        final JSONConfig config = new JSONConfig(configFile);
        ConfigWorkaround.init(config, TelegramBridgeConfig.class);
        config.read();
        ConfigWorkaround.onReload();
        config.save();
        logger = event.getModLog();

        context = new TelegramContext(logger);
        telegramLoop = new TelegramLoop(context);

        context.addListener(new ToMinecraftResender());
        context.addListener(new PlayerList());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        telegramLoop.start();
        if (!TelegramBridgeConfig.relay_level.server_start) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_start);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (!TelegramBridgeConfig.relay_level.server_stop) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_stop);
    }
}
