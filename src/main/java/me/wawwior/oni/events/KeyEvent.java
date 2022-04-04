package me.wawwior.oni.events;

import me.wawwior.utils.event.CancelableEvent;

public class KeyEvent extends CancelableEvent {

    private final int key;
    private final int scancode;

    private final int action;

    public KeyEvent(int key, int scancode, int action) {
        this.key = key;
        this.scancode = scancode;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }
}
