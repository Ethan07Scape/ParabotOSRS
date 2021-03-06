package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.Client;
import org.osrs.min.api.data.Calculations;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.data.Perspective;
import org.osrs.min.api.interfaces.Locatable;
import org.osrs.min.loading.Loader;

import java.awt.*;

public class Tile implements Locatable {
    int x;
    int y;
    int z;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = Loader.getClient().getClient_plane();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getRegionX() {
        return x - Game.getBaseX();
    }

    public int getRegionY() {
        return y - Game.getBaseY();
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(org.osrs.min.api.wrappers.Tile tile) {
        return Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public org.osrs.min.api.wrappers.Tile getLocation() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        final org.osrs.min.api.wrappers.Tile t = (org.osrs.min.api.wrappers.Tile) obj;
        return t.getX() == this.getX() && t.getY() == this.getY()
                && t.getZ() == this.getZ();
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY() + ", Z: " + getZ();
    }


    @Override
    public boolean canReach() {
        return false;

    }

    public Polygon getBounds() {
        Polygon polygon = new Polygon();
        Point pn = Perspective.tileToScreen(new org.osrs.min.api.wrappers.Tile(x, y, z), 0, 0, 0);
        Point px = Perspective.tileToScreen(new org.osrs.min.api.wrappers.Tile(x + 1, y, z), 0, 0, 0);
        Point py = Perspective.tileToScreen(new org.osrs.min.api.wrappers.Tile(x, y + 1, z), 0, 0, 0);
        Point pxy = Perspective.tileToScreen(new org.osrs.min.api.wrappers.Tile(x + 1, y + 1, z), 0, 0, 0);
        polygon.addPoint(py.x, py.y);
        polygon.addPoint(pxy.x, pxy.y);
        polygon.addPoint(px.x, px.y);
        polygon.addPoint(pn.x, pn.y);
        return polygon;
    }

    public Point getPointOnScreen() {
        return Perspective.tileToScreen(this);
    }

    public boolean isOffscreen() {
        final Client client = Loader.getClient();
        final Point point = getPointOnScreen();
        return (point.getX() < 0 || point.getX() >= client.getViewportWidth())
                && (point.getY() < 0 || point.getY() >= client.getViewportHeight());
    }

    public void draw(Graphics2D g, Color color) {
        if (isOffscreen())
            return;
        g.setColor(color);
        Polygon bounds = getBounds();
        if (bounds == null)
            return;
        g.drawPolygon(bounds);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g.fillPolygon(bounds);
    }

}
