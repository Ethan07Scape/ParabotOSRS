package org.osrs.min.ui.debugger;


import org.osrs.min.api.wrappers.Tile;

public class Entity {
    private int index, id, amount, distance;
    private String name;
    private Tile location;

    public Entity(int index, int id, int amount, String name, Tile location, int distance) {
        this.index = index;
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.location = location;
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Tile getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
    }
}
