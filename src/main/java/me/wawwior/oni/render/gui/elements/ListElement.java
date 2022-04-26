package me.wawwior.oni.render.gui.elements;

import me.wawwior.oni.render.gui.Element;

public class ListElement<U extends Element> extends ContainerElement<U> {

    public ListElement(String id, int x, int y, U parent) {
        super(id, x, y, 100, 20, parent);
    }
}
