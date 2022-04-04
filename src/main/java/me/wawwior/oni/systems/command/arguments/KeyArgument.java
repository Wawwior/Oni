package me.wawwior.oni.systems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.wawwior.oni.systems.command.CommandException;
import me.wawwior.oni.utils.KeyIndexer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KeyArgument implements ArgumentType<Integer> {

    private final List<String> valid = valid();

    private static List<String> valid() {
        List<String> valid = new ArrayList<>();
        KeyIndexer.KEYS.getNames(32, 314).forEach(s -> {
            if (!s.equalsIgnoreCase("ESCAPE")) {
                valid.add(s);
            }
        });
        return valid;
    }

    private KeyArgument() {

    }

    public static KeyArgument key() {
        return new KeyArgument();
    }

    public static Integer getKey(CommandContext<?> c, final String name) {
        return c.getArgument(name, Integer.class);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        String key = reader.readUnquotedString();
        if (!valid.contains(key)) {
            throw new CommandException("Invalid key: \"%s\"!", key);
        }
        return KeyIndexer.KEYS.getKey(key);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        valid.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
