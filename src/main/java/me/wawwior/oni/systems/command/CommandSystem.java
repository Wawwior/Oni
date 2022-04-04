package me.wawwior.oni.systems.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.wawwior.config.IConfig;
import me.wawwior.oni.Oni;
import me.wawwior.oni.systems.System;
import me.wawwior.oni.systems.command.commands.BindCommand;
import me.wawwior.oni.systems.command.commands.PrefixCommand;
import me.wawwior.oni.systems.command.commands.SayCommand;
import me.wawwior.oni.systems.command.commands.ToggleCommand;
import me.wawwior.utils.event.IEventListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandSystem extends System<CommandSystem.CommandsConfig> {

    public static class CommandsConfig implements IConfig {

        public String prefix = ".";
    }

    public boolean skipCommand = false;

    public final CommandSource COMMAND_SOURCE = new ChatCommandSource(Oni.MC);

    private final List<Command> commands = new ArrayList<>();

    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();
    private CommandDispatcher<CommandSource> dispatcher;

    public CommandSystem() {
        super("command", CommandsConfig.class);
    }

    @Override
    public void onLoad() {
        dispatcher = new CommandDispatcher<>();
        register(new PrefixCommand());
        register(new SayCommand());
        register(new ToggleCommand());
        register(new BindCommand());
    }

    public int onCommand(String command) throws CommandSyntaxException {
        ParseResults<CommandSource> parse = dispatcher.parse(command, COMMAND_SOURCE);
        try {
            return dispatcher.execute(parse);
        } catch (CommandSyntaxException e) {
            if (e instanceof CommandException) throw e;
            throw new CommandException("Unknown command: \"%s\"!", command.split(" ")[0]);
        }
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return dispatcher;
    }

    private void register(Command command) {
        if (commands.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) return;

        command.registerTo(dispatcher);
        commands.add(command);
        commandInstances.put(command.getClass(), command);

        if (command instanceof IEventListener) {
            Oni.EVENT_BUS.register((IEventListener) command);
            Oni.INSTANCE.getLogger().info("Registered Command \"" + command.getName() + "\" to EVENT_BUS");
        }
    }

    public void skipCommand() {
        skipCommand = true;
    }

    public List<Command> getAll() {
        return commands;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> _class) {
        return (T) commandInstances.get(_class);
    }

    private final static class ChatCommandSource extends ClientCommandSource {
        public ChatCommandSource(MinecraftClient client) {
            super(null, client);
        }
    }

    public static CommandSystem get() {
        return Oni.INSTANCE.getSystemManager().getInstance(CommandSystem.class);
    }

    public String getPrefix() {
        return config.prefix;
    }

    public void setPrefix(String prefix) {
        config.prefix = prefix;
    }

}
