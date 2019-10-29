package org.osrs.min.temp.canvas.screen;

import org.osrs.min.temp.canvas.listeners.PaintListener;
import org.parabot.environment.api.utils.Timer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ScreenOverlay<E> extends JCheckBoxMenuItem implements PaintListener {

    protected List<E> elements = new ArrayList<>();
    private Timer refreshRate = new Timer(1000);
    private boolean show;

    public ScreenOverlay(String name) {
        super(name);
        this.addActionListener(e -> show = isSelected());
    }

    public boolean activate() {
        return show;
    }

    public abstract E[] elements();

    public List<E> refresh() {
        if (!refreshRate.isRunning()) {
            elements = Arrays.asList(elements());
        }
        return elements;
    }

    public boolean isShow() {
        return show;
    }
}