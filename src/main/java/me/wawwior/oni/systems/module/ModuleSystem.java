/*
 * Copyright (c) 2021-2022 Wawwior
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

package me.wawwior.oni.systems.module;

import me.wawwior.config.IConfig;
import me.wawwior.oni.events.KeyEvent;
import me.wawwior.oni.systems.System;
import me.wawwior.oni.systems.module.modules.RandomModule;
import me.wawwior.utils.event.IEventListener;
import me.wawwior.utils.event.Subscribe;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleSystem extends System<ModuleSystem.ModulesConfig> implements IEventListener {

    private final ArrayList<Module<? extends ModuleConfig>> modules = new ArrayList<>();

    private final Map<Class<? extends Module>, Module<? extends ModuleConfig>> moduleInstances = new HashMap<>();

    public static class ModulesConfig implements IConfig {

    }

    public ModuleSystem() {
        super("module", ModulesConfig.class);
        init();
    }

    public void init() {
        register(new RandomModule());
    }

    public void register(Module<? extends ModuleConfig> module) {
        if (modules.stream().anyMatch(m -> m.name.equalsIgnoreCase(module.name))) return;
        modules.add(module);
        moduleInstances.put(module.getClass(), module);
    }

    @Override
    public void onLoad() {
        modules.forEach(Module::load);
    }

    @Override
    public void onUnload() {
        modules.forEach(Module::save);
    }

    @Subscribe
    public void toggle(KeyEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            modules.forEach(m -> {
                if (m.config.key == event.getKey()) m.toggle();
            });
        }
    }

    @SuppressWarnings("unchecked")
    public List<Module<? extends ModuleConfig>> getModules() {
        return (List<Module<? extends ModuleConfig>>) modules.clone();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module<? extends ModuleConfig>> T getInstance(Class<T> _class) {
        return (T) moduleInstances.get(_class);
    }
}
