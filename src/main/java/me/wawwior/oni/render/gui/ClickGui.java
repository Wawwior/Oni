package me.wawwior.oni.render.gui;

import me.wawwior.oni.render.gui.design.Design;
import me.wawwior.oni.render.gui.design.designs.DefaultDesign;
import me.wawwior.oni.render.gui.elements.ContainerElement;
import me.wawwior.oni.render.gui.elements.ListElement;
import me.wawwior.oni.systems.module.ModuleSystem;
import me.wawwior.oni.systems.module.modules.ClickGuiModule;
import me.wawwior.oni.utils.GuiUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ClickGui extends Screen {

    private static final ClickGui instance = new ClickGui();

    private final ContainerElement<?> element = new ContainerElement<>("click_gui", 0, 0,width, height, null);

    private Design design = new DefaultDesign();

    protected ClickGui() {
        super(Text.of("ClickGUI"));
        element.children.add(new ListElement<>("list", 10, 10, element));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        element.children.forEach(e -> {
            design.draw(new Design.RenderContext(matrices, mouseX, mouseY, delta), e);
        });
        //renderTooltip(matrices, new LiteralText("Mouse bound tooltip in clickgui"), mouseX, mouseY);
    }

    @Override
    public void onClose() {
        ModuleSystem.get().getInstance(ClickGuiModule.class).setToggled(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        element.children.forEach(e -> {
            e.onClick(mouseX, mouseY, GuiUtils.inBounds(mouseX, mouseY, e), button);
        });
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public Design getDesign() {
        return design;
    }

    public static ClickGui getInstance() {
        return instance;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
