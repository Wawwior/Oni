package me.wawwior.oni.utils;

import me.wawwior.oni.render.gui.Element;

public class GuiUtils {

    public static boolean inBounds(double x0, double y0, double x1, double y1, double width, double height) {
        return x0 > x1 && y0 > y1 && x0 < x1 + width && y0 < y1 + height;
    }

    public static boolean inBounds(double x0, double y0, Element e) {
        return inBounds(x0, y0, e.getX(), e.getY(), e.getWidth(), e.getHeight());
    }

}
