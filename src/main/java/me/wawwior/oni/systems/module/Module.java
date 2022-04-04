package me.wawwior.oni.systems.module;

import me.wawwior.config.Configurable;
import me.wawwior.oni.Oni;

public class Module<T extends ModuleConfig> extends Configurable<T> {

    private final String name;

    public Module(String name, int key, Class<T> configClass) {
        super(configClass, "modules/", name, Oni.INSTANCE.getConfigProvider());
        config.key = key;
        this.name = name;
    }

    public final void toggle() {
        config.toggled = !config.toggled;
        if (config.toggled) onEnable();
        else onDisable();
    }

    public final void setToggled(boolean toggled) {
        config.toggled = toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    protected void onEnable() {}

    protected void onDisable() {}

    void onTick() {}

    void onRender() {}

    public void bind(int key) {
        config.key = key;
    }

    public int getKey() {
        return config.key;
    }

    public String getName() {
        return name;
    }
}
