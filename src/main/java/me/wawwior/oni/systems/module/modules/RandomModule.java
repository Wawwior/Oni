package me.wawwior.oni.systems.module.modules;

import me.wawwior.oni.systems.module.Module;
import me.wawwior.oni.systems.module.ModuleConfig;
import me.wawwior.oni.utils.ChatUtils;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class RandomModule extends Module<RandomModule.RandomModuleConfig> {

    public static class RandomModuleConfig extends ModuleConfig {

    }

    public RandomModule() {
        super("random", GLFW.GLFW_KEY_V, RandomModuleConfig.class);
    }

    @Override
    protected void onEnable() {
        ChatUtils.chatLog(String.valueOf(ThreadLocalRandom.current().nextInt()));
        setToggled(false);
    }
}
