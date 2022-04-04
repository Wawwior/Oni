package me.wawwior.oni.systems.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandException extends CommandSyntaxException {
    public CommandException(String message, Object... args) {
        super(new SimpleCommandExceptionType(() -> ""), () -> "§c§o" + String.format(message, args));
    }
}
