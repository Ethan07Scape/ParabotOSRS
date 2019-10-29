package org.osrs.min.temp.canvas.inputs;


import org.osrs.min.temp.canvas.RSCanvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InternalKeyboard implements KeyListener {

    private Component component;
    private KeyListener keyDispatcher;
    @SuppressWarnings("unused")
    private KeyListener[] keyboardlistener;

    public InternalKeyboard(RSCanvas canvas) {
        if (canvas != null) {
            this.component = canvas;
            if (component.getKeyListeners() != null && component.getKeyListeners().length > 0 && component.getKeyListeners()[0] != null) {
                this.keyboardlistener = component.getKeyListeners();
                this.keyDispatcher = component.getKeyListeners()[0];
                for (final KeyListener keyListener : component.getKeyListeners()) {
                    component.removeKeyListener(keyListener);
                }
                component.addKeyListener(this);
            }
        }
    }

    public final KeyEvent create(int id, int keyCode, char c) {
        if (component == null)
            return null;
        return new KeyEvent(component, id, System.currentTimeMillis(), 0, keyCode, c,
                id != KeyEvent.KEY_TYPED ? KeyEvent.KEY_LOCATION_STANDARD : KeyEvent.KEY_LOCATION_UNKNOWN);
    }

    public void press(KeyEvent c) {
        if (component == null)
            return;
        component.dispatchEvent(c);
    }

    public void type(KeyEvent c) {
        if (component == null)
            return;
        component.dispatchEvent(c);
    }

    public void release(KeyEvent c) {
        if (component == null)
            return;
        component.dispatchEvent(c);
    }

    public void press(char c) {
        press(create(KeyEvent.KEY_PRESSED, c, c));
    }

    public void type(char c) {
        if (component == null)
            return;
        component.dispatchEvent(create(KeyEvent.KEY_TYPED, 0, c));
    }

    public void release(char c) {
        if (component == null)
            return;
        component.dispatchEvent(create(KeyEvent.KEY_RELEASED, c, c));
    }

    public KeyEvent createNew(KeyEvent old) {
        if (component == null)
            return null;
        return new KeyEvent(component, old.getID(), old.getWhen(), old.getModifiers(), old.getKeyCode(),
                old.getKeyChar(), old.getKeyLocation());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyDispatcher == null)
            return;
        keyDispatcher.keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keyDispatcher == null)
            return;
        keyDispatcher.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (keyDispatcher == null)
            return;
        keyDispatcher.keyTyped(createNew(e));
    }

}