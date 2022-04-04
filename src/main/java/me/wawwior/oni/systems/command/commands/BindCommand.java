package me.wawwior.oni.systems.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.wawwior.oni.events.KeyEvent;
import me.wawwior.oni.systems.command.Command;
import me.wawwior.oni.systems.command.arguments.KeyArgument;
import me.wawwior.oni.systems.command.arguments.ModuleArgumentType;
import me.wawwior.oni.systems.module.Module;
import me.wawwior.oni.utils.ChatUtils;
import me.wawwior.oni.utils.KeyIndexer;
import me.wawwior.utils.event.IEventListener;
import me.wawwior.utils.event.Priority;
import me.wawwior.utils.event.Subscribe;
import net.minecraft.command.CommandSource;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command implements IEventListener {


    public BindCommand() {
        super("bind");
    }

    private Module<?> await = null;

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                .then(
                        argument("module", ModuleArgumentType.module())
                                .executes(c -> {
                                    await = ModuleArgumentType.getModule(c, "module");
                                    ChatUtils.chatLog("Press [Esc] to unbind.");
                                    ChatUtils.chatLog("Waiting for Keybind...");
                                    return 1;
                                })
                                .then(
                                        argument("key", KeyArgument.key())
                                                .executes(c -> {
                                                    Module<?> module = ModuleArgumentType.getModule(c, "module");
                                                    module.bind(KeyArgument.getKey(c, "key"));
                                                    ChatUtils.chatLog("Set Keybind for " + module.getName() + " to " + KeyIndexer.KEYS.getName(KeyArgument.getKey(c, "key")));
                                                    return 1;
                                                })
                                )
                );
    }

    @Subscribe(Priority.HIGHEST)
    public void onKey(KeyEvent event) {
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (await != null) {
            if (event.getKey() == GLFW.GLFW_KEY_ESCAPE) {
                await.bind(GLFW.GLFW_KEY_UNKNOWN);
                event.setCanceled(true);
                ChatUtils.chatLog("Unbound " + await.getName() + "!");
                await = null;
                return;
            }
            await.bind(event.getKey());
            event.setCanceled(true);
            ChatUtils.chatLog("Set Keybind for " + await.getName() + " to " + KeyIndexer.KEYS.getName(event.getKey()));
            await = null;
        }
    }
}
