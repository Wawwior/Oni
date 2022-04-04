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

package me.wawwior.oni.systems;

import me.wawwior.config.IConfig;
import me.wawwior.oni.Oni;
import me.wawwior.oni.systems.command.CommandSystem;
import me.wawwior.oni.systems.module.ModuleSystem;
import me.wawwior.utils.event.IEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemManager {

    private static final List<System<? extends IConfig>> systems = new ArrayList<>();

    private static final Map<Class<? extends System>, System<? extends IConfig>> systemInstances = new HashMap<>();

    public static void init() {
        register(new ModuleSystem());
        register(new CommandSystem());
    }

    public static void register(System<? extends IConfig> s) {
        if (!systemInstances.containsKey(s.getClass())) {
            systemInstances.put(s.getClass(), s);
            systems.add(s);
            if (s instanceof IEventListener) {
                Oni.EVENT_BUS.register((IEventListener) s);
                Oni.INSTANCE.getLogger().info("Registered System \"" + s.getName() + "\" to EVENT_BUS");
            }
        }
    }

    public static void load() {
        systems.forEach(System::load);
        systems.forEach(System::onLoad);
    }

    public static void save() {
        systems.forEach(System::onUnload);
        systems.forEach(System::save);
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<? extends IConfig>> T getInstance(Class<T> _class) {
        return (T) systemInstances.get(_class);
    }
}
