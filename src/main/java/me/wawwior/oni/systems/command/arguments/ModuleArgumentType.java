package me.wawwior.oni.systems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.wawwior.oni.Oni;
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

    private static final ModuleSystem modSys = Oni.INSTANCE.getSystemManager().getInstance(ModuleSystem.class);

    private static final Map<String, Class<? extends Module>> modules = process(modSys.getModules());

    private static Map<String, Class<? extends Module>> process(List<Module<? extends ModuleConfig>> modules) {
        Map<String, Class<? extends Module>> map = new HashMap<>();
        modules.forEach(module -> map.put(module.getName(), module.getClass()));
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
            throw new CommandException("Invalid Module: \"%s\"!", name);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        modules.keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}
