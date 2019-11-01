package org.osrs.min.canvas.inputs.internals;

import org.osrs.min.canvas.RSCanvas;
import org.parabot.environment.api.utils.Time;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InternalMouse implements MouseListener, MouseMotionListener {

    private MouseListener mouseListenerDispatcher;
    private MouseMotionListener mouseMotionDispatcher;
    private Component component;
    private int clientX;
    private int clientY;
    private int clientPressX = -1;
    private int clientPressY = -1;
    private long clientPressTime = -1;
    private boolean clientPressed;
    private RSCanvas canvas;

    public InternalMouse(RSCanvas canvas) {
        if (canvas != null) {
            this.canvas = canvas;
            this.component = canvas;

            if (component.getMouseListeners() != null && component.getMouseListeners().length > 0 && component.getMouseListeners()[0] != null) {
                this.mouseListenerDispatcher = component.getMouseListeners()[0];
                this.mouseMotionDispatcher = component.getMouseMotionListeners()[0];
                component.addMouseListener(this);
                component.addMouseMotionListener(this);
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clientX = e.getX();
        clientY = e.getY();
        if (mouseListenerDispatcher == null)
            return;
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, clientX, clientY, 1, false, e.getButton()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        press(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clientPressX = e.getX();
        clientPressY = e.getY();
        clientPressTime = System.currentTimeMillis();
        clientPressed = false;
        release(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {

        Point p = canvas.getMousePosition();
        if (p != null && p.x > 0 && p.y > 0) {
            clientX = p.x;
            clientY = p.y;
            move(clientX, clientY);
        } else if (arg0.getX() > 0 && arg0.getY() > 0) {
            clientX = arg0.getX();
            clientY = arg0.getY();
            move(clientX, clientY);
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = canvas.getMousePosition();
        if (p != null && p.x > 0 && p.y > 0 && canvas != null && canvas.contains(p)) {
            clientX = p.x;
            clientY = p.y;
            move(clientX, clientY);

        } else if (e.getX() > 0 && e.getY() > 0 && canvas.contains(new Point(e.getX(), e.getY()))) {
            clientX = e.getX();
            clientY = e.getY();
        }
        move(clientX, clientY);
    }

    public int getX() {
        return clientX;
    }

    public int getY() {
        return clientY;
    }

    public int getPressX() {
        return clientPressX;
    }

    public int getPressY() {
        return clientPressY;
    }

    public long getPressTime() {
        return clientPressTime;
    }

    public boolean isPressed() {
        return clientPressed;
    }

    public Point getLocation() {
        return new Point(getX(), getY());
    }

    public void move(int x, int y) {
        clientX = x;
        clientY = y;
        if (mouseListenerDispatcher == null)
            return;
        mouseMotionDispatcher.mouseMoved(
                new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
    }

    public void click(int x, int y, int button) {
        press(x, y, button);
        Time.sleep(45);
        release(x, y, button);
        if (mouseListenerDispatcher == null)
            return;
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, clientX, clientY, 1, false, button));
    }

    public void press(int x, int y, int button) {
        if (mouseListenerDispatcher == null)
            return;
        mouseListenerDispatcher.mousePressed(new MouseEvent(component, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false, button));
    }

    public void release(int x, int y, int button) {
        if (mouseListenerDispatcher == null)
            return;
        mouseListenerDispatcher.mouseReleased(new MouseEvent(component, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false, button));
    }

    public void click(boolean right) {
        click(clientX, clientY, (right ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1));
    }

    public boolean drag(int x1, int y1, int x2, int y2) {
        move(x1, y1);
        press(getX(), getY(), MouseEvent.BUTTON2);
        move(x2, y2);
        release(getX(), getY(), MouseEvent.BUTTON2);

        return getX() == x2 && getY() == y2 && !isPressed();
    }

}