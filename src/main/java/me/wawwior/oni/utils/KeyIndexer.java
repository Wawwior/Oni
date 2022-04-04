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

package me.wawwior.oni.utils;

import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyIndexer {

    public static final KeyIndexer KEYS = new KeyIndexer();

    private final Map<Integer, String> names = new HashMap<>();
    private final Map<String, Integer> keys = new HashMap<>();

    public KeyIndexer() {
        index();
    }

    private void index() {
        for (Field field : GLFW.class.getFields()) {
            if (field.getName().startsWith("GLFW_KEY_")) {
                try {
                    names.put(field.getInt(null), field.getName().replace("GLFW_KEY_", ""));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        names.forEach((i, s) -> keys.put(s, i));
    }

    public String getName(int key) {
        return names.get(key);
    }

    public List<String> getNames() {
        return new ArrayList<>(names.values());
    }

    public List<String> getNames(int start, int end) {
        List<String> result = new ArrayList<>();
        names.forEach((i, s) -> {
            if (i > start && i < end) {
                result.add(s);
            }
        });
        return result;
    }

    public int getKey(String name) {
        return keys.get(name);
    }

}
