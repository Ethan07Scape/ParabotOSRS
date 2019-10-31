package org.osrs.min.api.definitions;


import org.osrs.min.loading.Loader;
import org.osrs.min.loading.hooks.XMLHookParser;

public class ItemDefinition {
    private final org.osrs.min.api.accessors.ItemDefinition accessor;
    private final int id;

    public ItemDefinition(int id) {
        this.id = id;
        this.accessor = Loader.getClient().getItemDefinition(id, XMLHookParser.getDummyValue("getItemDefinition"));
    }

    public String[] getInventoryActions() {
        if (!hasAccessor())
            return new String[0];
        return this.accessor.getActions();
    }

    public String[] getGroundActions() {
        if (!hasAccessor())
            return new String[0];
        return this.accessor.getGroundActions();
    }

    public String getName() {
        if (!hasAccessor())
            return "null";
        return this.accessor.getName();
    }

    protected boolean hasAccessor() {
        return accessor != null;
    }
}