package org.osrs.min.api.data;

import org.osrs.min.api.interactive.Interfaces;
import org.osrs.min.api.wrappers.InterfaceChild;
import org.osrs.min.api.wrappers.Item;
import org.osrs.min.utils.Utils;
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

        if (Bank.isOpen())
            return getBankInventory(filter);

        final InterfaceChild child = Interfaces.get(parentIndex, WIDGET_INVENTORY_SLOTS);

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

    private static Item[] getBankInventory(Filter<Item> filter) {
        final List<Item> list = new ArrayList<>();

        if (!Bank.isOpen())
            return list.toArray(new Item[list.size()]);

        final InterfaceChild parent = Interfaces.get(BANK_INVENTORY, WIDGET_BANK_INVENTORY_SLOTS);
        final InterfaceChild[] children;

        if (parent != null) {
            children = parent.getChildren();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    final InterfaceChild interfaceChild = children[i];
                    Item item = new Item(interfaceChild.getItemId(),
                            interfaceChild.getItemStackSize(), i, interfaceChild.getUID(), interfaceChild);
                    if (item.isValid() && item.getId() != 6512 && item.getStackSize() > 0 && (filter == null || filter.accept(item))) {
                        list.add(item);
                    }
                }
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
        final Item i = getItem(filter);
        return i != null && i.isValid();
    }

    public static boolean contains(int... ids) {
        final Item i = getItem(ids);
        return i != null && i.isValid();
    }

    public static boolean contains(String... names) {
        final Item i = getItem(names);
        return i != null && i.isValid();
    }

    public static int getCount(boolean countStackSize, Filter<Item> filter) {
        int count = 0;
        for (Item item : getAllItems(filter)) {
            count = count + (countStackSize ? item.getStackSize() : 1);
        }
        return count;
    }
    public static int getCount(boolean countStackSize, final String... names) {
        if (names == null)
            return 0;
        return getCount(countStackSize, item -> item.isValid() && item.getName() != null && Utils.getInstance().inArray(item.getName(), names));
    }

    public static int getCount() {
        return getCount(false);
    }

    public static int getCount(boolean countStackSize) {
        int count = 0;
        for (Item i : getAllItems()) {
            if (i.isValid()) {
                count = count + (countStackSize ? i.getStackSize() : 1);
            }
        }
        return count;
    }

    public static int getCount(boolean countStackSize, final int... ids) {
        if (ids == null)
            return 0;
        return getCount(countStackSize, item -> item.isValid() && Utils.getInstance().inArray(item.getId(), ids));
    }

    public static int getCount(final int... ids) {
        return getCount(false, ids);
    }

    public static int getCount(final String... names) {
        return getCount(false, names);
    }

    public static int getCount(Filter<Item> filter) {
        return getCount(false, filter);
    }

}
