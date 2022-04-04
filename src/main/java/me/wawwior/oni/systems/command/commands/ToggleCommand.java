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
