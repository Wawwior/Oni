package me.wawwior.oni.systems.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.wawwior.oni.systems.command.Command;
import me.wawwior.oni.systems.command.CommandSystem;
import net.minecraft.command.CommandSource;


public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(
                        argument("prefix", StringArgumentType.greedyString())
                                .executes(c -> {
                                    CommandSystem.get().setPrefix(c.getArgument("prefix", String.class));
                                    return 1;
                                })
                );
    }
}
