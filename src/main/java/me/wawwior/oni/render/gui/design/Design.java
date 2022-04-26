package me.wawwior.oni.render.gui.design;

import me.wawwior.oni.render.gui.Element;
import me.wawwior.oni.render.gui.elements.ContainerElement;
import me.wawwior.oni.render.gui.elements.ListElement;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Design {

    protected Map<Class<? extends Element>, List<Component>> elementRenderers = new HashMap<>(){{
        put(Element.class, new ArrayList<>());
        put(ContainerElement.class, new ArrayList<>(){{
            add((context, e) -> {
               ((ContainerElement<?>) e).children.forEach(c -> {
                   draw(context, c);
               });
            });
        }});
        put(ListElement.class, new ArrayList<>());
    }};


    public void draw(RenderContext context, Element e) {
        elementRenderers.get(e.getClass()).forEach(c -> c.draw(context, e));
    }

    public record RenderContext(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }

}
