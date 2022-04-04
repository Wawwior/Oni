package me.wawwior.oni.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.wawwior.oni.systems.command.CommandException;
import me.wawwior.oni.systems.command.CommandSystem;
import me.wawwior.oni.utils.ChatUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {

        String prefix = CommandSystem.get().getPrefix();

        if (message.startsWith(prefix) && !CommandSystem.get().skipCommand) {
            ci.cancel();
            try {
                int result = CommandSystem.get().onCommand(message.substring(prefix.length()));
            } catch (CommandSyntaxException e) {
                ChatUtils.chatLog(e.getMessage());
                if (!(e instanceof CommandException)) e.printStackTrace();
            }
        }

        CommandSystem.get().skipCommand = false;
    }
}
