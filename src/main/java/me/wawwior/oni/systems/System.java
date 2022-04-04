package me.wawwior.oni.systems;

import me.wawwior.config.Configurable;
import me.wawwior.config.IConfig;
import me.wawwior.oni.Oni;

/**
 * @author Wawwior
 */
public class System<T extends IConfig> extends Configurable<T> {

    private final String name;

    public System(String name, Class<T> configClass) {
        super(configClass, "", name, Oni.INSTANCE.getConfigProvider());
        this.name = name;
    }

    public void onLoad() {}

    public void onUnload() {}

    public String getName() {
        return name;
    }

}
