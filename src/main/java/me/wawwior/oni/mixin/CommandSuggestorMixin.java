/*
 * Copyright (c) 2021-2022 Wawwior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        String prefix = CommandSystem.get().config.prefix;
        if (reader.canRead() && reader.getString().startsWith(String.valueOf(prefix), reader.getCursor())) {
            reader.setCursor(reader.getCursor() + prefix.length());
            assert this.client.player != null;

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
