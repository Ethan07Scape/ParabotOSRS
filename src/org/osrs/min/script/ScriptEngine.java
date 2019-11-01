package org.osrs.min.script;

import org.osrs.min.api.data.Game;
import org.osrs.min.canvas.RSCanvas;
import org.osrs.min.canvas.screen.PaintListener;
import org.parabot.environment.scripts.Script;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class ScriptEngine {

    private final List<MouseListener> mouseListeners;
    private final List<MouseMotionListener> mouseMotionListeners;
    private PaintListener paintListener;
    private Script script = null;
    private RSCanvas canvas;

    public ScriptEngine() {
        this.mouseListeners = new ArrayList<>();
        this.mouseMotionListeners = new ArrayList<>();
    }

    public void addMouseListener(MouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void clearMouseListeners() {
        mouseListeners.clear();
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        mouseMotionListeners.add(mouseMotionListener);
    }

    public void clearMouseMotionListeners() {
        mouseMotionListeners.clear();
    }

    public void setScript(final Script script) {
        this.script = script;
    }

    public void unload() {
        clearMouseListeners();
        clearMouseMotionListeners();
        if (script instanceof PaintListener) {
            paintListener = (PaintListener) script;
            canvas.getPaintListeners().remove(paintListener);
            paintListener = null;
        }
        Game.setForcingAction(false);
        this.script = null;
    }

    public void init() {
        if (script == null) {
            throw new RuntimeException("Script is null");
        }
        if (script instanceof MouseListener) {
            addMouseListener((MouseListener) script);
        }
        if (script instanceof MouseMotionListener) {
            addMouseMotionListener((MouseMotionListener) script);
        }
        if (script instanceof PaintListener) {
            paintListener = (PaintListener) script;
            canvas.getPaintListeners().add(paintListener);
        }

    }

    public void dispatch(AWTEvent event) {
        if (this.script == null) {
            return;
        }
        if (!(event instanceof MouseEvent)) {
            return;
        }

        final MouseEvent e = (MouseEvent) event;
        for (final MouseListener m : mouseListeners) {
            switch (e.getID()) {
                case MouseEvent.MOUSE_CLICKED:
                    m.mouseClicked(e);
                    break;
                case MouseEvent.MOUSE_ENTERED:
                    m.mouseEntered(e);
                    break;
                case MouseEvent.MOUSE_EXITED:
                    m.mouseExited(e);
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    m.mousePressed(e);
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    m.mouseReleased(e);
            }
        }
        for (final MouseMotionListener m : mouseMotionListeners) {
            switch (e.getID()) {
                case MouseEvent.MOUSE_MOVED:
                    m.mouseMoved(e);
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    m.mouseDragged(e);
                    break;
            }
        }
    }

    public PaintListener getPaintListener() {
        return paintListener;
    }

    public int isPainting() {
        if(paintListener != null) {
            return 1;
        }
        return 0;
    }

    public RSCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(RSCanvas canvas) {
        this.canvas = canvas;
    }
}