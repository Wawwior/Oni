package me.wawwior.oni.render.gui.elements;

import me.wawwior.oni.render.gui.Element;
import me.wawwior.oni.render.gui.ElementConfig;

import java.util.ArrayList;
import java.util.List;

public class ContainerElement<U extends Element> extends Element<ElementConfig, U> {

    public List<Element> children = new ArrayList<>();

    public ContainerElement(String id, int x, int y, int width, int height, U parent) {
        super(id, ElementConfig.class, x, y, width, height, parent);
    }

}
