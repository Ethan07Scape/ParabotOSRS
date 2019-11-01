package org.osrs.min.api.data;

import org.osrs.min.api.interactive.Interfaces;
import org.osrs.min.api.wrappers.Interface;
import org.osrs.min.api.wrappers.InterfaceChild;
import org.osrs.min.api.wrappers.Item;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final static int BANK_PARENT_INTERFACE = 12;
    private static int BANK_ITEMS_CHILD = -1;

    public static Item[] getItems(Filter<Item> filter) {
        final List<Item> list = new ArrayList<>();

        if (!isOpen())
            return list.toArray(new Item[list.size()]);

        final InterfaceChild parent;
        final InterfaceChild[] children;

        if (BANK_ITEMS_CHILD != -1) {
            parent = Interfaces.get(BANK_PARENT_INTERFACE, BANK_ITEMS_CHILD);
        } else {
            parent = getItemsInterface();
        }
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

    public static Item[] getItems() {
        return getItems(i -> true);
    }

    public static boolean isOpen() {
        final InterfaceChild wc = Interfaces.get(12, 4);
        return wc != null && wc.isVisible();
    }

    /**
     * Sometimes OSRS changes the index of the child that holds the items, we need to be able to find it if it changes.
     */
    private static InterfaceChild getItemsInterface() {
        if (isOpen()) {
            final Interface parent = Interfaces.get(BANK_PARENT_INTERFACE);
            final InterfaceChild[] children = parent.getChildren();
            if (children != null) {
                for (InterfaceChild child : children) {
                    if (child.hasChildren() && child.getChildren() != null) {
                        for (InterfaceChild nextChild : child.getChildren()) {
                            if (interfaceHasWithdrawActions(nextChild)) {
                                BANK_ITEMS_CHILD = nextChild.getParentIndex();
                                return child;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean interfaceHasWithdrawActions(InterfaceChild child) {
        if (child == null)
            return false;
        final String[] actions = child.getActions();
        if (actions == null || actions.length <= 0)
            return false;
        for (String s : actions) {
            if (s != null && s.length() > 0 && !s.equalsIgnoreCase("null")) {
                if (s.equalsIgnoreCase("withdraw-all")) {
                    return true;
                }
            }
        }
        return false;
    }
}