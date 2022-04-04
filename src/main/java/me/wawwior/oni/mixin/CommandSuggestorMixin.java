package me.wawwior.oni.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.wawwior.oni.systems.command.CommandSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggestorMixin {

    @Shadow @Final MinecraftClient client;

    @Shadow private @Nullable ParseResults<CommandSource> parse;

    @Shadow @Final TextFieldWidget textField;

    @Shadow @Nullable CommandSuggestor.@Nullable SuggestionWindow window;

    @Shadow boolean completingSuggestions;

    @Shadow private @Nullable CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow protected abstract void show();

    @Inject(method = "refresh",
            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onRefresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = CommandSystem.get().getPrefix();
        if (client.player != null)
            if (reader.canRead() && reader.getString().startsWith(String.valueOf(prefix), reader.getCursor())) {
                reader.setCursor(reader.getCursor() + prefix.length());

                CommandDispatcher<CommandSource> commandDispatcher = CommandSystem.get().getDispatcher();
                if (this.parse == null) this.parse = commandDispatcher.parse(reader, CommandSystem.get().COMMAND_SOURCE);

                int cursor = textField.getCursor();
                if (cursor >= 1 && (this.window == null || !this.completingSuggestions)) {
                    this.pendingSuggestions = commandDispatcher.getCompletionSuggestions(this.parse, cursor);
                    this.pendingSuggestions.thenRun(() -> {
                       if (this.pendingSuggestions.isDone()) {
                           this.show();
                       }
                    });
                }
                ci.cancel();
            }
    }

}
