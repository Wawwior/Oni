package me.wawwior.oni.render.gui;

import me.wawwior.oni.systems.module.ModuleSystem;
import me.wawwior.oni.systems.module.modules.ClickGuiModule;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ClickGui extends Screen {

    private static final ClickGui instance = new ClickGui();

    protected ClickGui() {
        super(Text.of("ClickGUI"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, new LiteralText("Mouse bound tooltip in clickgui"), mouseX, mouseY);
    }

    @Override
    public void onClose() {
        ModuleSystem.get().getInstance(ClickGuiModule.class).setToggled(false);
    }

    public static ClickGui getInstance() {
        return instance;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
