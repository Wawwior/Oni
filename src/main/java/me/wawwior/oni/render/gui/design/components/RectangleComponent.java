package me.wawwior.oni.render.gui.design.components;

import me.wawwior.oni.render.gui.Element;
import me.wawwior.oni.render.gui.design.Component;
import me.wawwior.oni.render.gui.design.Design;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;

import java.awt.*;

public class RectangleComponent implements Component {

    private Color color;
    private int x, y;
    private int width, height;

    private int rel = 1;


    public RectangleComponent(Color color, int width, int height) {

        x = 0;
        y = 0;

        this.color = color;
        this.width = width;
        this.height = height;
    }

    public RectangleComponent(Color color, int x, int y, int width, int height) {

        this.x = x;
        this.y = y;

        this.color = color;
        this.width = width;
        this.height = height;
    }

    public RectangleComponent withAbsolutePosition() {
        rel = 0;
        return this;
    }

    public RectangleComponent withRelativePosition() {
        rel = 1;
        return this;
    }

    @Override
    public void draw(Design.RenderContext context, Element e) {
        //RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.clear();

        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder
                .vertex(e.getX() * rel + x, e.getY() * rel + y + height, 0)
                .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
                .next();
        bufferBuilder
                .vertex(e.getX() * rel + x + width, e.getY() * rel + y + height, 0)
                .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
                .next();
        bufferBuilder
                .vertex(e.getX() * rel + x + width, e.getY() * rel + y, 0)
                .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
                .next();
        bufferBuilder
                .vertex(e.getX() * rel + x, e.getY() * rel + y, 0)
                .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
                .next();

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);

    }
}
