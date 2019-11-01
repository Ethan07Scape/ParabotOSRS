package org.osrs.min.api.data;


public enum Spell {

    ENCHANT_LEVEL_1(7, 9),
    TELEPORT_TO_HOUSE(40, 24),
    TELEPORT_TO_CAMELOT(45, 27);
    private int levelRequired;
    private int childID;
    private int parentID = 218;

    Spell(int levelRequired, int childID) {
        this.levelRequired = levelRequired;
        this.childID = childID;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getChildID() {
        return childID;
    }

    public int getParentID() {
        return parentID;
    }
}
