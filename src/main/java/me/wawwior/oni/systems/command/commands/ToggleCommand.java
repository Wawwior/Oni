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

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.wawwior.oni.systems.command.Command;
import me.wawwior.oni.systems.command.arguments.ModuleArgumentType;
import net.minecraft.command.CommandSource;

public class ToggleCommand extends Command {


    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(
                        argument("module", ModuleArgumentType.module())
                                .executes(c -> {
                                    ModuleArgumentType.getModule(c, "module").toggle();
                                    return 1;
                                })
                                .then(
                                        argument("toggled", BoolArgumentType.bool())
                                                .executes(c -> {
                                                    ModuleArgumentType.getModule(c, "module").setToggled(BoolArgumentType.getBool(c, "toggled"));
                                                    return 1;
                                                })
                                )
                );
    }
}
