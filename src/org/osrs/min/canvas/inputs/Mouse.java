package org.osrs.min.canvas.inputs;

import org.osrs.min.canvas.inputs.internals.InternalMouse;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Mouse {
    private static Mouse instance;
    private InternalMouse mouse;
    private Map<Integer, Point> clicks = new HashMap<>();

    public Mouse(InternalMouse mouse) {
        this.mouse = mouse;
    }

    public static Mouse getInstance() {
        return instance;
    }

    public static void setInstance(Mouse instance) {
        Mouse.instance = instance;
    }

    public void move(int x, int y) {
        mouse.move(x, y);
    }

    public void move(Point p) {
        if (p == null)
            return;
        move(p.x, p.y);
    }

    public int getPressX() {
        return mouse.getPressX();
    }

    public int getPressY() {
        return mouse.getPressY();
    }

    public long getPressTime() {
        return mouse.getPressTime();
    }

    public boolean isPressed() {
        return mouse.isPressed();
    }

    public Point getLocation() {
        return mouse.getLocation();
    }

    public void click(boolean right) {
        mouse.click(right);
    }

    public void click(Point p, boolean right) {
        clicks.put(clicks.size() + 1, p);
        move(p);
        click(right);
    }

    public void press(int x, int y, int button) {
        mouse.press(x, y, button);
    }

    public void release(int x, int y, int button) {
        mouse.release(x, y, button);
    }

    public boolean dragMouse(Point p1, Point p2) {
        return dragMouse(p1.getLocation().x, p1.getLocation().y, p2.getLocation().x, p2.getLocation().y);
    }

    public boolean dragMouse(int x, int y, int x1, int y1) {
        return mouse.drag(x, y, x1, y1);
    }

    public Map<Integer, Point> getClicks() {
        return clicks;
    }
}