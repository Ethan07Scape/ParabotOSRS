package org.osrs.min.api.definitions;

public class NpcDefinition {
    private final org.osrs.min.api.accessors.NPCDefinition accessor;

    public NpcDefinition(org.osrs.min.api.accessors.NPCDefinition accessor) {
        this.accessor = accessor;
    }

    public final int getId() {
        if (!hasAccessor())
            return -1;
        return accessor.getId();
    }

    public final int getCombatLevel() {
        if (!hasAccessor())
            return -1;
        return accessor.getCombatLevel();
    }

    public final int getPrayerIcon() {
        if (!hasAccessor())
            return -1;
        return accessor.getHeadIconPrayer();
    }

    public final short[] getNewColors() {
        if (!hasAccessor())
            return null;
        return accessor.getRecolorTo();
    }

    public final String getName() {
        if (!hasAccessor())
            return "null";
        return accessor.getName();
    }

    public final String[] getActions() {
        if (!hasAccessor())
            return null;
        return accessor.getActions();
    }

    public final short[] getColors() {
        if (!hasAccessor())
            return null;
        return accessor.getRecolorFrom();
    }

    protected boolean hasAccessor() {
        return accessor != null;
    }
}
