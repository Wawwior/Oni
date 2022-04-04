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
