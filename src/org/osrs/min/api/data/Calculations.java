package org.osrs.min.api.data;

import org.osrs.min.api.interactive.Players;
import org.osrs.min.api.wrappers.Tile;

import java.awt.*;

public class Calculations {

    private static int distanceBetween(int x, int y, int x1, int y1) {
        return (int) Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }

    public static int distanceBetween(Point a, Point b) {
        return distanceBetween(a.x, a.y, b.x, b.y);
    }

    public static int distanceBetween(Tile a, Tile b) {
        return distanceBetween(a.getX(), a.getY(), b.getX(), b.getY());
    }

    public static int distanceTo(Tile a) {
        final Tile loc = Players.getMyPlayer().getLocation();
        return distanceBetween(a.getX(), a.getY(), loc.getX(), loc.getY());
    }

}
