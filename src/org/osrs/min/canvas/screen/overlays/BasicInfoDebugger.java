package org.osrs.min.canvas.screen.overlays;

import org.osrs.min.canvas.inputs.Mouse;
import org.osrs.min.canvas.screen.ScreenOverlay;
import org.osrs.min.loading.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoDebugger extends ScreenOverlay<String> {

    private final List<String> debuggedList = new ArrayList<>();

    public BasicInfoDebugger() {
        super("Game");
    }

    @Override
    public String[] elements() {
        debuggedList.clear();

        drawText("Mouse: [ X: " + Mouse.getInstance().getLocation().x + " - Y: " + Mouse.getInstance().getLocation().y + " ]");
        drawText("Energy: " + Loader.getClient().getRunEnergy());
        drawText("EngineCycle: " + Loader.getClient().getCycle());
        drawText("MembersWorld: " + Loader.getClient().getIsMembersWorld());
        drawText("Dest: " + Loader.getClient().getDestinationX() + " - " + Loader.getClient().getDestinationY());

        return debuggedList.toArray(new String[debuggedList.size()]);

    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(Color.orange);
        int yOff = 30;
        for (String str : elements()) {
            graphics.drawString(str, 15, yOff);
            yOff += 15;
        }
    }

    private void drawText(String debug) {
        debuggedList.add(debug);
    }

}