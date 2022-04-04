package me.wawwior.oni.systems;

import me.wawwior.config.IConfig;
import me.wawwior.oni.Oni;
import me.wawwior.oni.systems.command.CommandSystem;
import me.wawwior.oni.systems.module.ModuleSystem;
import me.wawwior.utils.event.IEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemManager {

    private final List<System<? extends IConfig>> systems = new ArrayList<>();

    private final Map<Class<? extends System>, System<? extends IConfig>> systemInstances = new HashMap<>();

    public SystemManager() {
        init();
    }

    private void init() {
        register(new ModuleSystem());
        register(new CommandSystem());
    }

    public void register(System<? extends IConfig> system) {
        if (systems.stream().anyMatch(s -> s.getName().equalsIgnoreCase(system.getName()))) return;

        systems.add(system);
        systemInstances.put(system.getClass(), system);

        if (system instanceof IEventListener) {
            Oni.EVENT_BUS.register((IEventListener) system);
            Oni.INSTANCE.getLogger().info("Registered System \"" + system.getName() + "\" to EVENT_BUS");
        }
    }

    public void load() {
        systems.forEach(System::load);
        systems.forEach(System::onLoad);
    }

    public void save() {
        systems.forEach(System::onUnload);
        systems.forEach(System::save);
    }

    @SuppressWarnings("unchecked")
    public <T extends System<? extends IConfig>> T getInstance(Class<T> _class) {
        return (T) systemInstances.get(_class);
    }
}
