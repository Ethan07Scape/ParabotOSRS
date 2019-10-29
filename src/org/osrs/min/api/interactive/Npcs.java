package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.Client;
import org.osrs.min.api.accessors.Npc;
import org.osrs.min.api.wrappers.Character;
import org.osrs.min.api.wrappers.NPC;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Npcs {

    private static final Comparator<NPC> NEAREST_SORTER = Comparator.comparingInt(Character::distanceTo);

    private static final Filter<NPC> ALL_FILTER = n -> true;

    public static final NPC[] getNpcs(final Filter<NPC> filter) {
        final Client client = Loader.getClient();
        List<NPC> npcList = new ArrayList<>();
        final Npc[] accNpcs = client.getNpcs();
        for (int i = 0; i < accNpcs.length; i++) {
            if (accNpcs[i] == null) {
                continue;
            }
            final NPC npc = new NPC(i, accNpcs[i]);
            if (filter.accept(npc)) {
                npcList.add(npc);
            }
        }

        return npcList.toArray(new NPC[npcList.size()]);
    }

    public static final NPC[] getNpcs() {
        return getNpcs(ALL_FILTER);
    }

    public static final NPC getClosest(final Filter<NPC> filter) {
        NPC[] npcs = getNearest(filter);
        if (npcs == null || npcs.length == 0) {
            return null;
        }

        return npcs[0];
    }

    public static NPC getClosest(int... ids) {
        NPC[] npcs = getNearest(ids);
        if (npcs == null || npcs.length == 0) {
            return null;
        }

        return npcs[0];
    }

    public static NPC getClosest(String... strings) {
        NPC[] npcs = getNearest(strings);
        if (npcs == null || npcs.length == 0) {
            return null;
        }

        return npcs[0];
    }

    public static final NPC[] getNearest(final Filter<NPC> filter) {
        final NPC[] npcs = getNpcs(filter);
        Arrays.sort(npcs, NEAREST_SORTER);

        return npcs;
    }

    public static final NPC[] getNearest(final int... ids) {
        final NPC[] npcs = getNpcs(npc -> {
            for (final int id : ids) {
                if (id == npc.getId()) {
                    return true;
                }
            }

            return false;
        });
        Arrays.sort(npcs, NEAREST_SORTER);

        return npcs;
    }

    public static final NPC[] getNearest(final String... strings) {
        final NPC[] npcs = getNpcs(npc -> {
            for (final String string : strings) {
                if (npc.getName().toLowerCase().equals(string.toLowerCase())) {
                    return true;
                }
            }

            return false;
        });
        Arrays.sort(npcs, NEAREST_SORTER);

        return npcs;
    }
    public static final NPC[] getNearest() {
        return getNearest(ALL_FILTER);
    }
}