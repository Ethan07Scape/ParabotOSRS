package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.InterfaceComponent;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interfaces.Interactable;

import java.util.ArrayList;
import java.util.List;

public class InterfaceChild implements Interactable {
    private final InterfaceComponent accessor;
    private final int index;
    private final int parent;
    private final InteractionHandler interactionHandler;

    public InterfaceChild(InterfaceComponent accessor, int index, int parent) {
        this.accessor = accessor;
        this.index = index;
        this.parent = parent;
        this.interactionHandler = new InteractionHandler(this);
    }

    public InterfaceChild getParent() {
        final InterfaceComponent parent = accessor.getParent();
        if (parent == null)
            return null;
        return new InterfaceChild(parent, parent.getComponentIndex(), getParentIndex());
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public int getRelativeX() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getRelativeX();
    }

    public int getRelativeY() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getRelativeY();
    }

    public int getHeight() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getHeight();
    }

    public int getWidth() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getWidth();
    }

    public int getItemId() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getItemId();
    }

    public int getType() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getType();
    }

    public int getRenderCycle() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getRenderCycle();
    }

    public int getItemStackSize() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getItemStackSize();
    }

    public int getComponentIndex() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getComponentIndex();
    }

    public int getButtonType() {
        if (getAccessor() == null)
            return this.accessor.getButtonType();
        return -1;
    }

    public int[] getItemIds() {
        if (getAccessor() == null)
            return new int[0];
        return this.accessor.getItemIds();
    }

    public int[] getItemStackSizes() {
        if (getAccessor() == null)
            return new int[0];
        return this.accessor.getItemStackSizes();
    }

    public String[] getActions() {
        if (getAccessor() == null)
            return new String[0];
        return this.accessor.getActions();
    }

    public String getSelectedAction() {
        if (getAccessor() == null)
            return null;
        return this.accessor.getSelectedAction();
    }

    public String getText() {
        if (getAccessor() == null)
            return null;
        return this.accessor.getText();
    }


    public boolean isExplicitlyHidden() {
        if (getAccessor() == null)
            return true;
        return this.accessor.getExplicitlyHidden();
    }

    public int getTextureID() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getTextColor();
    }

    public int getBoundsIndex() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getBoundsIndex();
    }

    public int getConfig() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getConfig();
    }

    public boolean isVisible() {
        if (getAccessor() == null)
            return false;
        if (this.getRenderCycle() + 20 < Game.getGameCycle()) return false;
        return !this.isExplicitlyHidden();
    }

    public int getUID() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getUid();
    }

    public int getParentUID() {
        if (getAccessor() == null)
            return -1;
        return this.accessor.getParentUid();
    }

    public int getParentIndex() {
        if (getAccessor() == null)
            return -1;
        if (getComponentIndex() == -1) return getUID() >>> 16;
        return getUID() & 65535;
    }

    public int getIndex() {
        if (getAccessor() == null)
            return -1;
        final int componentIndex = getComponentIndex();
        if (componentIndex == -1)
            return getUID() & 65535;
        return componentIndex;
    }

    public InterfaceComponent[] getComponents() {
        if (getAccessor() == null)
            return null;
        return this.accessor.getComponents();
    }

    public InterfaceChild getChild(int index) {
        if (getAccessor() == null)
            return null;
        final InterfaceComponent[] children = getComponents();
        if (children == null || children.length <= index)
            return new InterfaceChild(null, index, index);
        return new InterfaceChild(children[index], index, index);
    }

    public InterfaceChild[] getChildren() {
        if (getAccessor() == null)
            return null;
        if (!hasChildren())
            return null;

        final List<InterfaceChild> list = new ArrayList<>();
        final InterfaceComponent[] children = getComponents();
        if (children == null) {
            return list.toArray(new InterfaceChild[list.size()]);
        }
        for (int i = 0; i < children.length; i++) {
            list.add(new InterfaceChild(children[i], i, parent));
        }
        return list.toArray(new InterfaceChild[list.size()]);
    }

    public boolean hasChildren() {
        if (getAccessor() == null)
            return false;
        final InterfaceComponent[] children = getComponents();
        if (children == null)
            return false;
        return children.length > 0;
    }

    @Override
    public boolean interact(String action) {
        return interactionHandler.interact(action);
    }

    public boolean interact(int opcode) {
        return interactionHandler.interact(opcode);
    }

    public boolean click() {
        if (getAccessor() == null)
            return false;
        final List<String> actionList = new ArrayList<>();
        final String[] actions = getActions();
        if (actions == null || actions.length <= 0)
            return false;
        for (String s : actions) {
            if (s.length() > 0 && !s.equalsIgnoreCase("null")) {
                actionList.add(s);
            }
        }
        if (actionList.size() <= 0)
            return false;
        return interact(actionList.get(0));
    }


    protected InterfaceComponent getAccessor() {
        return this.accessor;
    }
}
