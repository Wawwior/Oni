package me.wawwior.oni.render.gui;

import me.wawwior.config.Configurable;
import me.wawwior.oni.Oni;
import org.jetbrains.annotations.Nullable;

public class Element<T extends ElementConfig, U extends Element> extends Configurable<T> {

    U parent;

    public Element(String id, Class<T> configClass, int x, int y, int width, int height, @Nullable U parent) {
        super(configClass, "gui/", id, Oni.INSTANCE.getConfigProvider());
        this.parent = parent;
        config.x = x;
        config.y = y;

        this.width = width;
        this.height = height;
    }

    int width, height;

    void onClick(double mouseX, double mouseY, boolean hovering, int button) {}

    public int getX() {
        return config.x;
    }

    public int getY() {
        return config.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public U getParent() {
        return parent;
    }
}
