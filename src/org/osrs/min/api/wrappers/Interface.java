package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.InterfaceComponent;

import java.util.ArrayList;
import java.util.List;

public class Interface {

    private InterfaceComponent[] accessor;
    private int index;

    public Interface(InterfaceComponent[] accessor, int index) {
        this.accessor = accessor;
        this.index = index;
    }

    public InterfaceChild[] getChildren() {
        List<InterfaceChild> list = new ArrayList<>();
        if (accessor == null)
            return list.toArray(new InterfaceChild[list.size()]);
        for (int i = 0; i < accessor.length; i++) {
            list.add(new InterfaceChild(accessor[i], i, index));
        }
        return list.toArray(new InterfaceChild[list.size()]);
    }

    public InterfaceChild getChild(int index) {
        if (accessor == null || accessor.length <= index) {
            return null;
        }
        return new InterfaceChild(accessor[index], index, index);
    }

    public int getIndex() {
        return index;
    }
}