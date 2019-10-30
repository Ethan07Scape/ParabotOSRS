package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.InterfaceComponent;
import org.osrs.min.api.data.Game;

import java.util.ArrayList;
import java.util.List;

public class WidgetChild {
    private final InterfaceComponent accessor;
    private final int index;
    private final int parent;

    public WidgetChild(InterfaceComponent accessor, int index, int parent) {
        this.accessor = accessor;
        this.index = index;
        this.parent = parent;
    }

    public org.osrs.min.api.wrappers.WidgetChild getParent() {
        final InterfaceComponent parent = accessor.getParent();
        if (parent == null)
            return null;
        return new org.osrs.min.api.wrappers.WidgetChild(parent, parent.getComponentIndex(), getParentIndex());
    }

    public boolean hasParent() {
        return getParent() != null;
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
            return -1;
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
        int componentIndex = getComponentIndex();
        if (componentIndex == -1)
            return getUID() & 65535;
        return componentIndex;
    }

    public InterfaceComponent[] getComponents() {
        if (getAccessor() == null)
            return null;
        return this.accessor.getComponents();
    }

    public org.osrs.min.api.wrappers.WidgetChild getChild(int index) {
        if (getAccessor() == null)
            return null;
        InterfaceComponent[] children = getComponents();
        if (children == null || children.length <= index)
            return new org.osrs.min.api.wrappers.WidgetChild(null, index, index);
        return new org.osrs.min.api.wrappers.WidgetChild(children[index], index, index);
    }

    public org.osrs.min.api.wrappers.WidgetChild[] getChildren() {
        if (getAccessor() == null)
            return null;
        List<org.osrs.min.api.wrappers.WidgetChild> list = new ArrayList<>();
        InterfaceComponent[] children = getComponents();
        if (children == null) {
            return list.toArray(new org.osrs.min.api.wrappers.WidgetChild[list.size()]);
        }
        for (int i = 0; i < children.length; i++) {
            list.add(new org.osrs.min.api.wrappers.WidgetChild(children[i], i, parent));
        }
        return list.toArray(new org.osrs.min.api.wrappers.WidgetChild[list.size()]);
    }


    protected InterfaceComponent getAccessor() {
        return this.accessor;
    }
}
