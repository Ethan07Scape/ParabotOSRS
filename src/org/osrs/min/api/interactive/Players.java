package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.Client;
import org.osrs.min.api.wrappers.Character;
import org.osrs.min.api.wrappers.Player;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Players {

    private static final Comparator<Player> NEAREST_SORTER = Comparator.comparingInt(Character::distanceTo);

    private static final Filter<Player> ALL_FILTER = p -> true;

    public static final Player[] getPlayers(final Filter<Player> filter) {
        final Client client = Loader.getClient();
        ArrayList<Player> playerList = new ArrayList<>();
        final org.osrs.min.api.accessors.Player[] accPlayers = client.getPlayers();
        for (int i = 0; i < accPlayers.length; i++) {
            if (accPlayers[i] == null) {
                continue;
            }
            final Player player = new Player(i, accPlayers[i]);
            if (filter.accept(player)) {
                playerList.add(player);
            }
        }

        return playerList.toArray(new Player[playerList.size()]);
    }

    public static final Player[] getPlayers() {
        return getPlayers(ALL_FILTER);
    }

    public static final Player[] getNearest(final Filter<Player> filter) {
        final Player[] players = getPlayers(filter);
        Arrays.sort(players, NEAREST_SORTER);
        return players;
    }

    public static final Player[] getNearest() {
        return getNearest(ALL_FILTER);
    }

    public static Player getMyPlayer() {
        return new Player(-1, Loader.getClient().getPlayer());
    }
}
