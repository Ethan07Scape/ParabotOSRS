package org.osrs.min.api.wrappers;


import org.osrs.min.api.accessors.Username;

public class Player extends Character {
    private final int index;
    private final org.osrs.min.api.accessors.Player accessor;

    public Player(int index, org.osrs.min.api.accessors.Player accessor) {
        super(index, accessor);
        this.index = index;
        this.accessor = accessor;
    }

    private Username getNamePair() {
        if (accessor == null)
            return null;
        return this.accessor.getUsername();
    }

    public String getName() {
        if (accessor == null)
            return null;
        return getNamePair().getName();
    }

    public int getPrayerIcon() {
        if (accessor == null)
            return -1;
        return this.accessor.getHeadIconPrayer();
    }

    public int getSkullIcon() {
        if (accessor == null)
            return -1;
        return this.accessor.getHeadIconPk();
    }

    public int getTeam() {
        if (accessor == null)
            return -1;
        return this.accessor.getTeam();
    }

    public int getTotalLevel() {
        if (accessor == null)
            return -1;
        return this.accessor.getSkillLevel();
    }

    public int getCombatLevel() {
        if (accessor == null)
            return -1;
        return this.accessor.getCombatLevel();
    }

    @Override
    protected org.osrs.min.api.accessors.Player getAccessor() {
        return accessor;
    }

    public int getIndex() {
        return index;
    }
}
