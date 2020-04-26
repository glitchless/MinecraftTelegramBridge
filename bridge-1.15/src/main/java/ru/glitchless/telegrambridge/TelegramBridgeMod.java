package ru.glitchless.telegrambridge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.glitchless.telegrambridge.config.ConfigWorkaround;
import ru.glitchless.telegrambridge.config.ConfigWrapperImpl;
import ru.glitchless.telegrambridge.config.TelegramBridgeConfig;
import ru.glitchless.telegrambridge.core.config.ConfigWrapper;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramContext;
import ru.glitchless.telegrambridge.core.telegramapi.TelegramLoop;
import ru.glitchless.telegrambridge.handlers.PlayerList;
import ru.glitchless.telegrambridge.handlers.ToMinecraftResender;
import ru.glitchless.telegrambridge.handlers.ToTelegramEvent;

@Mod(TelegramBridgeMod.MODID)
public class TelegramBridgeMod {
    public static final String MODID = "telegrambridge";
    public static final String NAME = "Telegram Bridge";
    public static final String VERSION = "1.0";
    public static final String UPDATE_URL = "https://www.curseforge.com/minecraft/mc-mods/telegram-bridge";

    private static Logger logger = LogManager.getLogger();
    private static TelegramContext context;
    private static TelegramLoop telegramLoop;
    private static ConfigWrapper config = new ConfigWrapperImpl();

    public TelegramBridgeMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Pair<ConfigWorkaround, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigWorkaround::new);
        ConfigWorkaround.loadConfig(specPair.getRight(), FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static TelegramContext getContext() {
        return context;
    }


    public void setup(final FMLCommonSetupEvent event) {
        context = new TelegramContext(logger, config);
        telegramLoop = new TelegramLoop(context, config);

        context.addListener(new ToMinecraftResender());
        context.addListener(new PlayerList());
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        telegramLoop.start();
        if (!TelegramBridgeConfig.relay_level.server_start) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_start);
        checkUpdate();
    }

    private void checkUpdate() {
        if (VersionChecker.getResult(ModList.get().getModFileById(MODID).getMods().get(0)).status
                == VersionChecker.Status.OUTDATED) {
            ToTelegramEvent.broadcastToChats("There's a new update for the mod! Download it [here](" + UPDATE_URL + ")!");
        }
    }

    @SubscribeEvent
    public void serverStopping(FMLServerStoppingEvent event) {
        if (!TelegramBridgeConfig.relay_level.server_stop) {
            return;
        }
        ToTelegramEvent.broadcastToChats(TelegramBridgeConfig.text.server_stop);
    }
}
