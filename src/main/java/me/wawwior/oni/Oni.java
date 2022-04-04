
/*
 * Copyright (c) 2022-2022 Wawwior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.wawwior.oni;

import me.wawwior.config.ConfigProvider;
import me.wawwior.utils.event.EventBus;
import me.wawwior.oni.systems.SystemManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

/**
 * @author Wawwior
 */
@Environment(EnvType.CLIENT)
public class Oni implements ClientModInitializer {

    public static MinecraftClient MC;

    //TODO - TEMP: Main Chat Color
    public static String CHAT_COLOR = "ad4e4e";

    public static Oni INSTANCE;

    private final ConfigProvider configProvider = new ConfigProvider("./oni/");

    public static final EventBus EVENT_BUS = new EventBus();

    private final Logger logger = LogManager.getLogger();

    private final int color = new Color(227, 68, 68).getRGB();

    @Override
    public void onInitializeClient() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        INSTANCE = this;

        logger.info("Initializing...");

        MC = MinecraftClient.getInstance();

        SystemManager.init();
        SystemManager.load();

    }

    private void onShutdown() {
        SystemManager.save();
    }

    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    public Logger getLogger() {
        return logger;
    }

    public int getColor() {
        return color;
    }
}
