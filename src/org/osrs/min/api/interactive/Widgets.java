package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.InterfaceComponent;
import org.osrs.min.api.wrappers.Widget;
import org.osrs.min.api.wrappers.WidgetChild;
import org.osrs.min.loading.Loader;

public class Widgets {

    private static InterfaceComponent[][] getWidgets() {
        return Loader.getClient().getInterfaces();
    }

    public static Widget[] get() {
        InterfaceComponent[][] widgets = getWidgets();
        if (widgets == null)
            return new Widget[0];
        Widget[] children = new Widget[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            children[i] = new Widget(widgets[i], i);
        }
        return children;
    }

    public static WidgetChild get(int parent, int child) {
        Widget widgets = get(parent);
        if (widgets == null)
            return null;
        return widgets.getChild(child);
    }

    public static Widget get(int parent) {
        InterfaceComponent[][] widgets = getWidgets();
        if (widgets.length == 0 || (widgets.length - 1) < parent || parent < 0)
            return new Widget(null, parent);
        return new Widget(widgets[parent], parent);
    }

}
