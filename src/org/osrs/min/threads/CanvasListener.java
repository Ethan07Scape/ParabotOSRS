package org.osrs.min.threads;

import org.osrs.min.canvas.RSCanvas;
import org.osrs.min.canvas.inputs.Keyboard;
import org.osrs.min.canvas.inputs.Mouse;
import org.osrs.min.canvas.inputs.internals.InternalKeyboard;
import org.osrs.min.canvas.inputs.internals.InternalMouse;
import org.osrs.min.canvas.screen.ScreenOverlay;
import org.osrs.min.script.ScriptEngine;
import org.parabot.environment.api.utils.Time;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

public class CanvasListener extends Thread {

    private Applet applet;
    private RSCanvas canvas;
    private List<ScreenOverlay> overlays;
    private ScriptEngine scriptEngine;
    private boolean setInputs;

    /**
     * This thread waits until the applet has all its components loaded,
     * after they are loaded we add our keyboard/mouse to it.
     * <p>
     * After all that, we need to listen for the canvas values getting reset.
     * They get reset when focus on client is lost.
     */
    public CanvasListener(ScriptEngine scriptEngine, Applet applet, List<ScreenOverlay> overlays) {
        this.scriptEngine = scriptEngine;
        this.applet = applet;
        this.overlays = overlays;
    }

    public void run() {
        try {
            while ((canvas = getGameCanvas()) == null) {
                System.out.println("Sleeping til Canvas is found");
                Time.sleep(100);
            }
            SwingUtilities.invokeLater(() -> {
                try {
                    this.canvas.setOverlays(overlays);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Time.sleep(() -> applet.getComponents().length != 0, 10000);
            Time.sleep(() -> canvas.getMouseListeners() != null && canvas.getMouseListeners().length > 0, 10000);
            if (applet.getComponents().length > 0) {
                if (canvas.getMouseListeners() != null && canvas.getMouseListeners().length > 0) {
                    System.out.println("Set Mouse!");
                    System.out.println("Set Keyboard!");
                    Mouse.setInstance(new Mouse(new InternalMouse(canvas)));
                    Keyboard.setInstance(new Keyboard(new InternalKeyboard(canvas)));
                    setInputs = true;
                }
                this.scriptEngine.setCanvas(canvas);
            }
            while (true) {
                if (getGameCanvas() == null || overlays == null)
                    continue;
                int expectedSize = overlays.size() + scriptEngine.isPainting();
                int currentSize = getGameCanvas().getPaintListeners().size();
                if (currentSize != expectedSize) {
                    System.out.println("Resetting screen overlays...");
                    getGameCanvas().setOverlays(overlays);
                    if (scriptEngine.isPainting() == 1) {
                        if (!getGameCanvas().getPaintListeners().contains(scriptEngine.getPaintListener())) {
                            getGameCanvas().getPaintListeners().add(scriptEngine.getPaintListener());
                            System.out.println("Resetting paint overlays...");
                        }
                    }
                }
                if (!hasMouse()) {
                    Mouse.setInstance(new Mouse(new InternalMouse(getGameCanvas())));
                }
                if (!hasKeyboard()) {
                    Keyboard.setInstance(new Keyboard(new InternalKeyboard(getGameCanvas())));
                }
                Time.sleep(() -> getGameCanvas().getPaintListeners() == null || getGameCanvas().getPaintListeners().size() == 0, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasMouse() {
        if (getGameCanvas().getMouseListeners() != null && getGameCanvas().getMouseListeners().length > 0) {
            for (MouseListener ml : getGameCanvas().getMouseListeners()) {
                if (ml instanceof InternalMouse) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasKeyboard() {
        if (getGameCanvas().getKeyListeners() != null && getGameCanvas().getKeyListeners().length > 0) {
            for (KeyListener kl : getGameCanvas().getKeyListeners()) {
                if (kl instanceof InternalKeyboard) {
                    return true;
                }
            }
        }
        return false;
    }
    public Applet getApplet() {
        return applet;
    }

    public synchronized RSCanvas getGameCanvas() {
        if (getApplet() == null || getApplet().getComponentCount() == 0 || !(getApplet().getComponent(0) instanceof RSCanvas)) {
            return null;
        }
        return (RSCanvas) getApplet().getComponent(0);
    }
}
