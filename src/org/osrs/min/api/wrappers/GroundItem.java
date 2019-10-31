package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.Pickable;
import org.osrs.min.api.data.Calculations;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.definitions.ItemDefinition;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interfaces.Interactable;
import org.osrs.min.api.interfaces.Locatable;


public class GroundItem implements Locatable, Interactable {

    private Pickable accessor;
    private int x;
    private int y;
    private int id;
    private ItemDefinition itemDefinition;
    private InteractionHandler interactionHandler;

    public GroundItem(Pickable accessor, int x, int y) {
        this.accessor = accessor;
        this.x = x;
        this.y = y;
        this.id = getId();
        this.interactionHandler = new InteractionHandler(this);
    }

    public int getX() {
        return Game.getBaseX() + x;
    }

    public int getY() {
        return Game.getBaseY() + y;
    }

    public int getSceneX() {
        return x;
    }

    public int getSceneY() {
        return y;
    }

    public int getStackSize() {
        return this.accessor.getStackSize();
    }

    public int getId() {
        return this.accessor.getId();
    }

    public String getName() {
        if (getDefinition() == null)
            return "null";
        return getDefinition().getName();
    }

    public ItemDefinition getDefinition() {
        if (itemDefinition != null)
            return itemDefinition;
        itemDefinition = new ItemDefinition(id);
        return itemDefinition;
    }


    @Override
    public int distanceTo() {
        return Calculations.distanceTo(getLocation());
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY(), Game.getPlane());
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
        if (getDefinition() == null)
            return new String[0];
        return getDefinition().getGroundActions();
    }

    protected Pickable getAccessor() {
        return accessor;
    }

}