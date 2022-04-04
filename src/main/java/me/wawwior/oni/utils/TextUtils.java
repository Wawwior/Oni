package me.wawwior.oni.utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Arrays;

public class TextUtils {

    public static Text merge(Text... texts) {
        MutableText out = new LiteralText("");
        for (Text txt : texts) {
            out.append(txt);
        }
        return out;
    }

    public static Text withColor(Text txt, int rgb) {
        return txt.copy().setStyle(Style.EMPTY.withColor(rgb));
    }

    public static Text parse(String s) {
        MutableText out = new LiteralText("");
        String[] parts = s.split("§#");
        out.append(Text.of(parts[0]));
        for (String part : Arrays.asList(parts).subList(1, parts.length)) {
            try {
                out.append(withColor(Text.of(part.substring(6)), Integer.decode("0x" + part.substring(0, 6)) + (15 << 24)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.append(Text.of(part.substring(6)));
            }
        }
        return out;
    }

}
