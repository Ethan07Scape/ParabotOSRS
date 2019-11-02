package org.osrs.min.api.wrappers;


import org.osrs.min.api.definitions.ItemDefinition;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interfaces.Interactable;

public class Item implements Interactable {
    private int index;
    private int id;
    private int stackSize;
    private int hash = 0;
    private InterfaceChild interfaceChild;
    private ItemDefinition itemDefinition;
    private InteractionHandler interactionHandler;

    public Item(int id, int stackSize, int index, int hash, InterfaceChild interfaceChild) {
        this.id = id;
        this.stackSize = stackSize;
        this.index = index;
        this.hash = hash;
        this.interfaceChild = interfaceChild;
        this.interactionHandler = new InteractionHandler(this);
    }

    public int getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        if (getItemDefinition() == null)
            return "null";
        return getItemDefinition().getName();
    }

    public String[] getInventoryActions() {
        if (getItemDefinition() == null)
            return new String[0];
        return getItemDefinition().getInventoryActions();
    }

    public String[] getGroundActions() {
        if (getItemDefinition() == null)
            return new String[0];
        return getItemDefinition().getGroundActions();
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getHash() {
        return hash;
    }

    public InterfaceChild getParentInterface() {
        return interfaceChild;
    }

    public ItemDefinition getItemDefinition() {
        try {
            if (itemDefinition == null)
                itemDefinition = new ItemDefinition(getId());
            return itemDefinition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValid() {
        return id > 0 && stackSize > 0;
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
        if (getItemDefinition() == null)
            return new String[0];
        return getItemDefinition().getInventoryActions();
    }
}
