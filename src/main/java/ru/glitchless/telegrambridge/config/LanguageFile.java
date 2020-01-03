package ru.glitchless.telegrambridge.config;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.glitchless.telegrambridge.TelegramBridgeMod;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LanguageFile {
    private static final String DEFAULT_LANG_FILE_PATH = "/lang/" + TelegramBridgeMod.MODID + ".lang";

    @Nonnull
    public static Properties getLanguageFile(FMLPreInitializationEvent event) {
        InputStream is = LanguageFile.class.getResourceAsStream("/assets/" + TelegramBridgeMod.MODID + "/lang/en_us.lang");
        final Properties defaults = new Properties();
        try {
            defaults.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final File configFile = new File(event.getModConfigurationDirectory().getAbsoluteFile(), DEFAULT_LANG_FILE_PATH);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            OutputStream os = null;
            try {
                os = new FileOutputStream(configFile);
                defaults.store(os, "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        final Properties properties = new Properties(defaults);
        try {
            is = new FileInputStream(configFile);
            properties.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return properties;
    }
}
