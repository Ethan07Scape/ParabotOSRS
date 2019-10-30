package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.InterfaceComponent;

import java.util.ArrayList;
import java.util.List;

public class Widget {

    private InterfaceComponent[] accessor;
    private int index;

    public Widget(InterfaceComponent[] accessor, int index) {
        this.accessor = accessor;
        this.index = index;
    }

    public WidgetChild[] getChildren() {
        List<WidgetChild> list = new ArrayList<>();
        if (accessor == null)
            return list.toArray(new WidgetChild[list.size()]);
        for (int i = 0; i < accessor.length; i++) {
            list.add(new WidgetChild(accessor[i], i, index));
        }
        return list.toArray(new WidgetChild[list.size()]);
    }

    public WidgetChild getChild(int index) {
        if (accessor == null || accessor.length <= index) {
            return new WidgetChild(null, index, index);
        }
        return new WidgetChild(accessor[index], index, index);
    }


    public int getIndex() {
        return index;
    }
}