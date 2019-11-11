package org.osrs.min.api.data;


import org.osrs.min.loading.Loader;

public enum Skills {

    ATTACK(0), DEFENSE(1), STRENGTH(2), HITPOINTS(3), RANGE(4), PRAYER(5),
    MAGIC(6), COOKING(7), WOODCUTTING(8), FLETCHING(9), FISHING(10), FIREMAKING(11), CRAFTING(12), SMITHING(13), MINING(14), HERBLORE(15), AGILITY(16), THIEVING(17),
    SLAYER(18), FARMING(19), RUNECRAFTING(20), HUNTER(21), CONSTRUCTION(22);

    private static final int[] XP_TABLE = {0, 0, 83, 174, 276, 388, 512, 650,
            801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
            3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431,
            14391160, 15889109, 17542976, 19368992, 21385073, 23611006,
            26068632, 28782069, 31777943, 35085654, 38737661, 42769801,
            47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
            85539082, 94442737, 104273167};

    private int index;

    Skills(int index) {
        this.index = index;
    }

    public static int getExpAtLevel(int level) {
        if (level > 120) {
            return -1;
        }
        return XP_TABLE[level];
    }

    public static int getLevelAtExp(int xp) {
        for (int i = 0; i < XP_TABLE.length; i++) {
            if (xp - XP_TABLE[i] > 0) {
                return i;
            }
        }
        return XP_TABLE[XP_TABLE.length - 1];
    }

    private final int[] getSkillExpArray() {
        return Loader.getClient().getExperience();
    }

    private final int[] getSkillLevelArray() {
        return Loader.getClient().getCurrentLevels();
    }

    private final int[] getRealSkillLevelArray() {
        return Loader.getClient().getLevels();
    }

    public final int getRealLevel() {
        int[] realLevels = getRealSkillLevelArray();
        if (realLevels == null) {
            return 99;
        }
        return realLevels[index];
    }

    public final int getCurrentLevel() {
        int[] skillLevelArray = getSkillLevelArray();
        if (skillLevelArray == null)
            return 1;

        return skillLevelArray[index];
    }

    public final int getExperience() {
        int[] skillLevelArray = getSkillExpArray();
        if (getSkillExpArray() == null)
            return 0;
        return skillLevelArray[index];
    }

    public final int getExpTillNextLevel() {
        return getExpAtLevel(getRealLevel() + 1) - getExperience();
    }

}