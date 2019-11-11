package org.osrs.min.api.definitions;

import org.osrs.min.loading.Loader;
import org.osrs.min.loading.hooks.XMLHookParser;


public class ObjectDefinition {

    private org.osrs.min.api.accessors.ObjectDefinition accessor;
    private int dummyValue = -1;

    public ObjectDefinition(int id) {
        final byte b = (byte) getDummyValue();
        accessor = Loader.getClient().getObjectDefinition(id, b);
    }

    public String[] getActions() {
        if (!hasAccessor())
            return new String[0];
        return this.accessor.getActions();
    }

    public String getName() {
        if (!hasAccessor())
            return "null";
        return this.accessor.getName();
    }

    public short[] getColors() {
        if (!hasAccessor())
            return new short[0];
        return this.accessor.getRecolorFrom();

    }

    public short[] getNewColors() {
        if (!hasAccessor())
            return new short[0];
        return this.accessor.getRecolorTo();

    }

    public int getId() {
        if (!hasAccessor())
            return -1;
        return accessor.getId();
    }

    public int getDummyValue() {
        if (dummyValue == -1)
            dummyValue = XMLHookParser.getDummyValue("getObjectDefinition");
        return dummyValue;
    }

    protected boolean hasAccessor() {
        return accessor != null;
    }

}