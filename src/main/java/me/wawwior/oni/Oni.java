package me.wawwior.oni;

import me.wawwior.config.ConfigProvider;
import me.wawwior.config.io.impl.FileInfo;
import me.wawwior.config.io.impl.JsonFileAdapter;
import me.wawwior.oni.events.ShutdownEvent;
import me.wawwior.oni.systems.SystemManager;
import me.wawwior.utils.event.EventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class Oni implements ClientModInitializer {

    public static MinecraftClient MC;

    //TODO - TEMP: Main Chat Color
    public static String CHAT_COLOR = "ad4e4e";

    public static Oni INSTANCE;

    private final ConfigProvider<FileInfo> configProvider = new ConfigProvider<>(new JsonFileAdapter("./oni/"), false);

    private SystemManager systemManager;

    public static final EventBus EVENT_BUS = new EventBus();

    private final Logger logger = LogManager.getLogger();

    //private final String color = Integer.toHexString(new Color(227, 68, 68).getRGB()).substring(2);

    @Override
    public void onInitializeClient() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        INSTANCE = this;

        logger.info("Initializing...");

        MC = MinecraftClient.getInstance();

        systemManager = new SystemManager();

        systemManager.load();

    }

    private void onShutdown() {
        EVENT_BUS.post(new ShutdownEvent());
        systemManager.save();
    }

    public SystemManager getSystemManager() {
        return systemManager;
    }

    public ConfigProvider<FileInfo> getConfigProvider() {
        return configProvider;
    }

    public Logger getLogger() {
        return logger;
    }
}
