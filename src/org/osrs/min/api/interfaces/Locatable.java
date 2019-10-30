package org.osrs.min.api.interfaces;


import org.osrs.min.api.wrappers.Tile;

public interface Locatable {

    int distanceTo();

    int distanceTo(org.osrs.min.api.interfaces.Locatable locatable);

    int distanceTo(Tile tile);

    Tile getLocation();

    boolean canReach();

}
