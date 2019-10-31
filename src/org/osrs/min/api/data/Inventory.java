package org.osrs.min.api.data;

import org.osrs.min.api.interactive.Widgets;
import org.osrs.min.api.wrappers.Item;
import org.osrs.min.api.wrappers.WidgetChild;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final int WIDGET_INVENTORY_SLOTS = 0;
    private static final int WIDGET_BANK_INVENTORY_SLOTS = 3;
    private static int INVENTORY_INDEX = 149;
    private static int BANK_INVENTORY = 15;
    private static int SHOP_INVENTORY = 301;

    public static Item[] getAllItems(Filter<Item> filter) {
        int parentIndex = INVENTORY_INDEX;
        final List<Item> list = new ArrayList<>();
        if (!Game.isLoggedIn())
            return list.toArray(new Item[list.size()]);

        final WidgetChild child = Widgets.get(parentIndex, WIDGET_INVENTORY_SLOTS);

        if (child == null)
            return list.toArray(new Item[list.size()]);

        final int[] contentIds = child.getItemIds();

        final int[] stackSizes = child.getItemStackSizes();

        if (contentIds == null || stackSizes == null)
            return list.toArray(new Item[list.size()]);

        for (int itemIndex = 0; itemIndex < contentIds.length; itemIndex++) {
            Item item = new Item(contentIds[itemIndex] - 1, stackSizes[itemIndex], itemIndex,
                    child.getUID(), child);
            if (item.isValid() && (filter == null || filter.accept(item))) {
                list.add(item);
            }
        }
        return list.toArray(new Item[list.size()]);
    }

    public static Item[] getAllItems() {
        return getAllItems(item -> true);
    }

    public static Item getItem(Filter<Item> filter) {
        Item[] items = getAllItems(filter);

        if (items == null || items.length == 0)
            return null;

        return items[0];
    }

    public static Item[] getItems(final int... ids) {
        return getAllItems(e -> {
            for (int id : ids) {
                if (e.getId() == id) {
                    return true;
                }
            }
            return false;
        });
    }

    public static Item getItem(int... id) {
        for (Item i : getItems(id)) {
            if (i != null) {
                return i;
            }
        }
        return null;
    }

    public static Item getItem(String... name) {
        for (Item i : getItems(name)) {
            if (i != null) {
                return i;
            }
        }
        return null;
    }

    public static Item[] getItems(final String... names) {
        return getAllItems(e -> {
            for (String name : names) {
                if (e.getName().toLowerCase().equals(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        });
    }

    public static int getFreeSpace() {
        return 28 - getAllItems().length;
    }

    public static boolean isFull() {
        return getCount() == 28;
    }

    public static boolean isEmpty() {
        return getCount() == 0;
    }

    public static boolean contains(Filter<Item> filter) {
        return getItem(filter).isValid();
    }

    public static boolean contains(int... ids) {
        return getItem(ids).isValid();
    }

    public static boolean contains(String... names) {
        return getItem(names).isValid();
    }

    public static int getCount() {
        return getCount(false);
    }

    public static int getCount(int... ids) {
        return getCount(false, ids);
    }

    public static int getCount(final boolean includeStack, int... ids) {
        int count = 0;
        int parentIndex = INVENTORY_INDEX;

        final WidgetChild child = Widgets.get(parentIndex, WIDGET_INVENTORY_SLOTS);

        if (child == null)
            return -1;

        final int[] items = child.getItemIds();
        final int[] stackSizes = includeStack ? child.getItemStackSizes() : null;
        for (int i = 0; i < items.length; i++) {
            final int itemId = items[i];
            if (itemId > 0) {
                for (final int id : ids) {
                    if (id == itemId - 1) {
                        count += includeStack ? stackSizes[i] : 1;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public static int getCount(final boolean includeStack) {
        int count = 0;
        int parentIndex = INVENTORY_INDEX;

        final WidgetChild child = Widgets.get(parentIndex, WIDGET_INVENTORY_SLOTS);

        if (child == null)
            return -1;

        final int[] items = child.getItemIds();
        final int[] stackSizes = includeStack ? child.getItemStackSizes() : null;
        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0) {
                count += includeStack ? stackSizes[i] : 1;
            }
        }
        return count;
    }
}
