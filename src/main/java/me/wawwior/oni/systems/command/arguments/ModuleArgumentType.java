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

package me.wawwior.oni.systems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.wawwior.oni.systems.SystemManager;
import me.wawwior.oni.systems.command.CommandException;
import me.wawwior.oni.systems.module.Module;
import me.wawwior.oni.systems.module.ModuleConfig;
import me.wawwior.oni.systems.module.ModuleSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("rawtypes")
public class ModuleArgumentType implements ArgumentType<Module> {

    private ModuleArgumentType() {}

    private static final ModuleSystem modSys = SystemManager.getInstance(ModuleSystem.class);

    private static final Map<String, Class<? extends Module>> modules = process(modSys.getModules());

    private static Map<String, Class<? extends Module>> process(List<Module<? extends ModuleConfig>> modules) {
        Map<String, Class<? extends Module>> map = new HashMap<>();
        modules.forEach(module -> map.put(module.name, module.getClass()));
        return map;
    }

    public static ModuleArgumentType module() {
        return new ModuleArgumentType();
    }

    public static Module getModule(CommandContext<?> context, final String name) {
        return context.getArgument(name, Module.class);
    }

    @Override
    public Module parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        if (modules.get(name) != null)
            return modSys.getInstance(modules.get(name));
        else
            throw new CommandException("Invalid Module: " + name + "!");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        modules.keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}
