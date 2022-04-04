package me.wawwior.oni.systems.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.wawwior.oni.Oni;
import me.wawwior.oni.systems.command.Command;
import me.wawwior.oni.systems.command.CommandSystem;
import net.minecraft.command.CommandSource;

public class SayCommand extends Command {


    public SayCommand() {
        super("say");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(
                        argument("msg", StringArgumentType.greedyString())
                                .executes(c -> {
                                    assert Oni.MC.player != null;
                                    CommandSystem.get().skipCommand();
                                    Oni.MC.player.sendChatMessage(c.getArgument("msg", String.class));
                                    return 1;
                                })
                );
    }
}
