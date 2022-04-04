/*
 * Copyright (c) 2022-2022 Wawwior
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

package me.wawwior.oni.systems.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.wawwior.oni.events.KeyEvent;
import me.wawwior.oni.systems.command.Command;
import me.wawwior.oni.systems.command.arguments.KeyArgument;
import me.wawwior.oni.systems.command.arguments.ModuleArgumentType;
import me.wawwior.oni.systems.module.Module;
import me.wawwior.oni.utils.ChatUtils;
import me.wawwior.oni.utils.KeyIndexer;
import me.wawwior.utils.event.IEventListener;
import me.wawwior.utils.event.Priority;
import me.wawwior.utils.event.Subscribe;
import net.minecraft.command.CommandSource;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command implements IEventListener {


    public BindCommand() {
        super("bind");
    }

    private Module<?> await = null;

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(
                        argument("module", ModuleArgumentType.module())
                                .executes(c -> {
                                    await = ModuleArgumentType.getModule(c, "module");
                                    ChatUtils.chatLog("Press [Esc] to unbind.");
                                    ChatUtils.chatLog("Waiting for Keybind...");
                                    return 1;
                                })
                                .then(
                                        argument("key", KeyArgument.key())
                                                .executes(c -> {
                                                    Module<?> module = ModuleArgumentType.getModule(c, "module");
                                                    module.bind(KeyArgument.getKey(c, "key"));
                                                    ChatUtils.chatLog("Set Keybind for " + module.name + " to " + KeyIndexer.KEYS.getName(KeyArgument.getKey(c, "key")));
                                                    return 1;
                                                })
                                )
                );
    }

    @Subscribe(Priority.HIGHEST)
    public void onKey(KeyEvent event) {
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (await != null) {
            if (event.getKey() == GLFW.GLFW_KEY_ESCAPE) {
                await.bind(GLFW.GLFW_KEY_UNKNOWN);
                event.setCanceled(true);
                ChatUtils.chatLog("Unbound " + await.name + "!");
                await = null;
                return;
            }
            await.bind(event.getKey());
            event.setCanceled(true);
            ChatUtils.chatLog("Set Keybind for " + await.name + " to " + KeyIndexer.KEYS.getName(event.getKey()));
            await = null;
        }
    }
}
