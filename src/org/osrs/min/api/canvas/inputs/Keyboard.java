package org.osrs.min.api.canvas.inputs;

import org.parabot.environment.api.utils.Time;

import java.awt.event.KeyEvent;


public class Keyboard {
    public static Keyboard instance = null;
    private InternalKeyboard keyboard;

    public Keyboard(InternalKeyboard keyboard) {
        this.keyboard = keyboard;
    }

    public static Keyboard getInstance() {
        return instance;
    }

    public static void setInstance(Keyboard instance) {
        Keyboard.instance = instance;
    }

    public void pressEnter() {
        press(KeyEvent.VK_ENTER);
        release(KeyEvent.VK_ENTER);
    }

    public void press(int event) {
        this.keyboard.press(this.keyboard.create(KeyEvent.KEY_PRESSED, event, (char) event));
    }

    public void release(int event) {
        this.keyboard.release(this.keyboard.create(KeyEvent.KEY_RELEASED, event, (char) event));
    }

    public void type(char c) {
        this.keyboard.type(c);
    }

    public void sendText(String text, boolean pressEnter) {
        for (int i = 0; i < text.toCharArray().length; i++) {
            type(text.toCharArray()[i]);
            Time.sleep(100);
        }
        if (pressEnter)
            pressEnter();
    }
}