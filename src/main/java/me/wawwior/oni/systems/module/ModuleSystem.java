package me.wawwior.oni.systems.module;

import me.wawwior.config.IConfig;
import me.wawwior.oni.Oni;
import me.wawwior.oni.events.KeyEvent;
import me.wawwior.oni.systems.System;
import me.wawwior.oni.systems.module.modules.ClickGuiModule;
import me.wawwior.oni.systems.module.modules.RandomModule;
import me.wawwior.utils.event.IEventListener;
import me.wawwior.utils.event.Subscribe;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleSystem extends System<ModuleSystem.ModulesConfig> implements IEventListener {

    private final ArrayList<Module<? extends ModuleConfig>> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module<? extends ModuleConfig>> moduleInstances = new HashMap<>();

    public static class ModulesConfig implements IConfig {

    }

    public ModuleSystem() {
        super("modules", ModulesConfig.class);
    }

    public void register(Module<? extends ModuleConfig> module) {
        if (modules.stream().anyMatch(m -> m.getName().equalsIgnoreCase(module.getName()))) return;

        modules.add(module);
        moduleInstances.put(module.getClass(), module);

        if (module instanceof IEventListener) {
            Oni.EVENT_BUS.register((IEventListener) module);
            Oni.INSTANCE.getLogger().info("Registered Module \"" + module.getName() + "\" to EVENT_BUS");
        }
    }

    @Override
    public void onLoad() {
        register(new RandomModule());
        register(new ClickGuiModule());
    }

    @Subscribe
    public void toggle(KeyEvent event) {
        if (Oni.MC.currentScreen instanceof ChatScreen) return;
        if (event.getAction() == GLFW.GLFW_PRESS) {
            modules.forEach(m -> {
                if (m.getKey() == event.getKey()) m.toggle();
            });
        }
    }

    public static ModuleSystem get() {
        return Oni.INSTANCE.getSystemManager().getInstance(ModuleSystem.class);
    }

    @SuppressWarnings("unchecked")
    public List<Module<? extends ModuleConfig>> getModules() {
        return (List<Module<? extends ModuleConfig>>) modules.clone();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module<? extends ModuleConfig>> T getInstance(Class<T> _class) {
        return (T) moduleInstances.get(_class);
    }
}
