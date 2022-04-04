
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
