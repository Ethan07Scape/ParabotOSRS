package org.osrs.min.api.wrappers;



import org.osrs.min.api.definitions.NpcDefinition;

public class NPC extends Character {

    private org.osrs.min.api.accessors.NPC accessor;

    private int index;

    public NPC(int index, org.osrs.min.api.accessors.NPC accessor) {
        super(index, accessor);
        this.index = index;
        this.accessor = accessor;
    }

    public final NpcDefinition getDefinition() {
        return new NpcDefinition(accessor.getDefinition());
    }

    public int getId() {
        if (getDefinition() == null)
            return -1;
        return getDefinition().getId();
    }

    public String getName() {
        if (getDefinition() == null)
            return "null";
        return getDefinition().getName();
    }

    public String[] getActions() {
        if (getDefinition() == null)
            return null;
        return getDefinition().getActions();
    }

    public short[] getColors() {
        if (getDefinition() == null)
            return null;
        return getDefinition().getColors();
    }

    public short[] getNewColors() {
        if (getDefinition() == null)
            return null;
        return getDefinition().getNewColors();
    }

    public int getPrayerIcon() {
        if (getDefinition() == null)
            return -1;
        return getDefinition().getPrayerIcon();
    }

    public int getCombatLevel() {
        if (getDefinition() == null)
            return -1;
        return getDefinition().getCombatLevel();
    }

    @Override
    public org.osrs.min.api.accessors.NPC getAccessor() {
        return accessor;
    }

    public int getIndex() {
        return index;
    }
}
