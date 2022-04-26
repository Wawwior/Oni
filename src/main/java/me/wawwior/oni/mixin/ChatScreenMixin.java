package me.wawwior.oni.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("ConstantConditions")
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow
    CommandSuggestor commandSuggestor;

    @Shadow
    protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    /**
     * @author Wawwior
     */
    @Overwrite()
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.commandSuggestor.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.setScreen(null);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            String string = this.chatField.getText().trim();
            if (!string.isEmpty()) {
                this.sendMessage(string);
            }
            // Mixin Start
            if (this.client.currentScreen instanceof ChatScreen)
                this.client.setScreen(null);
            //Mixin End
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.setChatFromHistory(-1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.setChatFromHistory(1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            this.client.inGameHud.getChatHud().scroll(this.client.inGameHud.getChatHud().getVisibleLineCount() - 1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            this.client.inGameHud.getChatHud().scroll(-this.client.inGameHud.getChatHud().getVisibleLineCount() + 1);
            return true;
        }
        return false;
    }

    @Shadow
    public abstract void setChatFromHistory(int offset);

}
