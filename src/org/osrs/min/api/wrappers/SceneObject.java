package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.EntityMarker;
import org.osrs.min.api.data.Calculations;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.definitions.ObjectDefinition;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interfaces.Interactable;
import org.osrs.min.api.interfaces.Locatable;

public class SceneObject implements Locatable, Interactable {

    private EntityMarker accessor;
    private int x;
    private int y;
    private int z;
    private int id;
    private ObjectDefinition objectDefinition;
    private InteractionHandler interactionHandler;

    public SceneObject(EntityMarker accessor, int x, int y, int z) {
        this.accessor = accessor;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = getId();
        this.interactionHandler = new InteractionHandler(this);
    }

    public String getName() {
        ObjectDefinition objectDefinition = this.getDefinition();
        if (objectDefinition != null) {
            return objectDefinition.getName();
        }
        return null;
    }

    public ObjectDefinition getDefinition() {
        if (objectDefinition != null)
            return objectDefinition;
        objectDefinition = new ObjectDefinition(id);
        return objectDefinition;
    }

    public int getLocalX() {
        return x;
    }


    public int getLocalY() {
        return y;
    }

    public int getId() {
        final long var10000 = this.getUID();
        final int var10001 = ((14 ^ 12) << 4 | 21) ^ 13 ^ 41;
        return (int) (var10000 >>> var10001 & ((3160638303L & 398248300L | 146959257L) & 212112086L ^ 4083121451L));
    }


    public long getUID() {
        return this.accessor.getUid();
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this.getLocation());
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(tile, getLocation());
    }

    public Tile getLocation() {
        return new Tile(getLocalX() + Game.getBaseX(), getLocalY() + Game.getBaseY(), Game.getPlane());
    }

    @Override
    public boolean canReach() {
        return getLocation().canReach();
    }

    @Override
    public boolean interact(String action) {
        return interactionHandler.interact(action);
    }

    public boolean interact(int opcode) {
        return interactionHandler.interact(opcode);
    }

    @Override
    public String[] getActions() {
        ObjectDefinition objectDefinition = this.getDefinition();
        if (objectDefinition != null) {
            return objectDefinition.getActions();
        }
        return new String[0];
    }

}