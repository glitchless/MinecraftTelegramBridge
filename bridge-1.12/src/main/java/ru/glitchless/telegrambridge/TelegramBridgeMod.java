package ru.glitchless.telegrambridge;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.ConfigWrapperImpl;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.config.ConfigWrapper;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramLoop;
import ru.glitchless.telegrambridge.handlers.PlayerList;
import ru.glitchless.telegrambridge.handlers.ToMinecraftResender;
import ru.glitchless.telegrambridge.handlers.ToTelegramEvent;

@Mod(modid = TelegramBridgeMod.MODID,
        name = TelegramBridgeMod.NAME,
        version = TelegramBridgeMod.VERSION,
        updateJSON = "https://raw.githubusercontent.com/glitchless/MinecraftTelegramBridge/master/static/update.json",
        acceptableRemoteVersions = "*")
public class TelegramBridgeMod {
    public static final String MODID = "telegrambridge";
    public static final String NAME = "Telegram Bridge";
    public static final String VERSION = "1.0";
    public static final String UPDATE_URL = "https://www.curseforge.com/minecraft/mc-mods/telegram-bridge";

    private static Logger logger;
    private static TelegramContext context;
    private static TelegramLoop telegramLoop;
    private static final ConfigWrapper config = new ConfigWrapperImpl();

    public static TelegramContext getContext() {
        return context;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        context = new TelegramContext(logger, config);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide() != Side.SERVER
                && TelegramBridgeConfig.server_only) {
            return;
        }
        telegramLoop = new TelegramLoop(context, config);
        telegramLoop.start();

        context.addListener(new ToMinecraftResender());
        context.addListener(new PlayerList());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (!TelegramBridgeConfig.relay_level.server_start) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_start);
        checkUpdate();
    }

    private void checkUpdate() {
        if (ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(this)).status
                == ForgeVersion.Status.OUTDATED) {
            ToTelegramEvent.broadcastToChats("There's a new update for the mod! Download it [here](" + UPDATE_URL + ")!");
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (!TelegramBridgeConfig.relay_level.server_stop) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_stop);
    }
}
