package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.NodeDeque;
import org.osrs.min.api.accessors.Pickable;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.wrappers.GroundItem;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroundItems {

    private static final Comparator<GroundItem> NEAREST_SORTER = Comparator.comparingInt(GroundItem::distanceTo);
    private static final Filter<GroundItem> ALL_FILTER = item -> true;

    public final static GroundItem[] getGroundItems(final Filter<GroundItem> filter) {
        final List<GroundItem> items = new ArrayList<>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                final NodeDeque deque = Loader.getClient().getPickableNodeDeques()[Game.getPlane()][x][y];
                if (deque != null) {
                    for (Pickable pickable = (Pickable) deque.current(); pickable != null; pickable = (Pickable) deque.next()) {
                        final GroundItem groundItem = new GroundItem(pickable, x, y);
                        if (filter.accept(groundItem))
                            items.add(new GroundItem(pickable, x, y));
                    }
                }
            }
        }
        return items.toArray(new GroundItem[items.size()]);
    }

    public static final GroundItem[] getGroundItems() {
        return getGroundItems(ALL_FILTER);
    }

    public static final GroundItem getClosest(final Filter<GroundItem> filter) {
        GroundItem[] objects = getNearest(filter);
        if (objects == null || objects.length == 0) {
            return null;
        }

        return objects[0];
    }

    public static final GroundItem getClosest(int... ids) {
        GroundItem[] objects = getNearest(ids);
        if (objects == null || objects.length == 0) {
            return null;
        }

        return objects[0];
    }

    public static final GroundItem getClosest(String... names) {
        GroundItem[] objects = getNearest(names);
        if (objects == null || objects.length == 0) {
            return null;
        }

        return objects[0];
    }

    public static final GroundItem[] getNearest(Filter<GroundItem> filter) {
        final GroundItem[] objects = getGroundItems(filter);
        Arrays.sort(objects, NEAREST_SORTER);

        return objects;
    }

    public static final GroundItem[] getNearest() {
        return getNearest(ALL_FILTER);
    }

    public static final GroundItem[] getNearest(final int... ids) {
        return getNearest(object -> {
            for (final int id : ids) {
                if (id == object.getId()) {
                    return true;
                }
            }

            return false;
        });
    }

    public static final GroundItem[] getNearest(final String... names) {
        return getNearest(object -> {
            for (final String name : names) {
                if (name.toLowerCase().equals(object.getName().toLowerCase())) {
                    return true;
                }
            }

            return false;
        });
    }
}