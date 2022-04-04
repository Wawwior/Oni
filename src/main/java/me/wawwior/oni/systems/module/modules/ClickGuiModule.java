package me.wawwior.oni.systems.module.modules;

import me.wawwior.oni.Oni;
import me.wawwior.oni.events.ShutdownEvent;
import me.wawwior.oni.render.gui.ClickGui;
import me.wawwior.oni.systems.module.Module;
import me.wawwior.oni.systems.module.ModuleConfig;
import me.wawwior.utils.event.IEventListener;
import me.wawwior.utils.event.Subscribe;
import org.lwjgl.glfw.GLFW;

public class ClickGuiModule extends Module<ClickGuiModule.ClickGuiConfig> implements IEventListener {

    boolean hudHidden = false;

    public static class ClickGuiConfig extends ModuleConfig {

    }

    public ClickGuiModule() {
        super("clickgui", GLFW.GLFW_KEY_RIGHT_SHIFT, ClickGuiConfig.class);
    }

    @Override
    protected void onEnable() {
        hudHidden = Oni.MC.options.hudHidden;
        Oni.MC.options.hudHidden = true;
        Oni.MC.setScreen(ClickGui.getInstance());
    }

    @Override
    protected void onDisable() {
        Oni.MC.options.hudHidden = hudHidden;
        Oni.MC.setScreen(null);
    }

    @Subscribe
    public void onShutdown(ShutdownEvent e) {
        Oni.MC.options.hudHidden = hudHidden;
        config.toggled = false;
    }
}
