package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.InterfaceComponent;
import org.osrs.min.api.wrappers.Interface;
import org.osrs.min.api.wrappers.InterfaceChild;
import org.osrs.min.loading.Loader;

public class Interfaces {

    private static InterfaceComponent[][] getWidgets() {
        return Loader.getClient().getInterfaces();
    }

    public static Interface[] get() {
        InterfaceComponent[][] widgets = getWidgets();
        if (widgets == null)
            return new Interface[0];
        Interface[] children = new Interface[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            children[i] = new Interface(widgets[i], i);
        }
        return children;
    }

    public static InterfaceChild get(int parent, int child) {
        Interface widgets = get(parent);
        if (widgets == null)
            return null;
        return widgets.getChild(child);
    }

    public static Interface get(int parent) {
        InterfaceComponent[][] widgets = getWidgets();
        if (widgets.length == 0 || (widgets.length - 1) < parent || parent < 0)
            return new Interface(null, parent);
        return new Interface(widgets[parent], parent);
    }

}
