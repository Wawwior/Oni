package me.wawwior.oni.utils;

import me.wawwior.oni.Oni;
import net.minecraft.text.Text;

public class ChatUtils {

    public static void sendMsg(Text msg) {
        Oni.MC.inGameHud.getChatHud().addMessage(msg);
    }

    public static void chatLog(String txt) {
        Oni.MC.inGameHud.getChatHud().addMessage(TextUtils.parse("§#" + Oni.CHAT_COLOR + "§oOni §7§o>> " + txt));
    }

}
