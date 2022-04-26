package me.wawwior.oni.render.gui.design.designs;

import me.wawwior.oni.render.gui.design.Design;
import me.wawwior.oni.render.gui.design.components.RectangleComponent;
import me.wawwior.oni.render.gui.elements.ListElement;

import java.awt.*;

public class DefaultDesign extends Design {

    public DefaultDesign() {
        elementRenderers.get(ListElement.class).add(new RectangleComponent(new Color(0xffffffff), 100, 20));
    }

}
