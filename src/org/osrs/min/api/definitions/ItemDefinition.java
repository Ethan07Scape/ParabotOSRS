package org.osrs.min.api.definitions;


import org.osrs.min.loading.Loader;
import org.osrs.min.loading.hooks.XMLHookParser;

import java.util.HashMap;
import java.util.Map;

public class ItemDefinition {

    private static Map<Integer, org.osrs.min.api.accessors.ItemDefinition> itemDefinitionCache;
    private final int id;
    private org.osrs.min.api.accessors.ItemDefinition accessor;
    private int dummyValue = -1;

    public ItemDefinition(int id) {
        if (ItemDefinition.itemDefinitionCache == null)
            ItemDefinition.itemDefinitionCache = new HashMap<>();

        this.id = id;

        if (id <= 0 || getDummyValue() == -1)
            this.accessor = null;

        this.accessor = getItemDefinition();
    }

    public org.osrs.min.api.accessors.ItemDefinition getItemDefinition() {
        if (accessor == null) {
            if (ItemDefinition.itemDefinitionCache.containsKey(id)) {
                accessor = itemDefinitionCache.get(id);
            } else {
                accessor = Loader.getClient().ItemDefinition_get(id, getDummyValue());
                ItemDefinition.itemDefinitionCache.putIfAbsent(id, this.accessor);
            }
        }
        return accessor;
    }

    public String[] getInventoryActions() {
        if (!hasAccessor())
            return new String[0];
        return this.accessor.getInventoryActions();
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

    public int getDummyValue() {
        if (dummyValue == -1)
            dummyValue = XMLHookParser.getDummyValue("getItemDefinition");
        return dummyValue;
    }
    protected boolean hasAccessor() {
        return accessor != null;
    }
}