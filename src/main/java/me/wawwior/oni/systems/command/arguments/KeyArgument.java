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
            throw new CommandException("Invalid key: " + key + "!");
        }
        return KeyIndexer.KEYS.getKey(key);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        valid.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
