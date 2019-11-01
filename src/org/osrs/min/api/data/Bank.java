package org.osrs.min.api.data;

import org.osrs.min.api.interactive.Interfaces;
import org.osrs.min.api.interactive.Npcs;
import org.osrs.min.api.interactive.SceneObjects;
import org.osrs.min.api.wrappers.*;
import org.osrs.min.utils.Utils;
import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final static int BANK_PARENT_INTERFACE = 12;
    private static int BANK_ITEMS_CHILD = -1;

    public static boolean open() {
        final SceneObject[] objects = SceneObjects.getNearest("Bank");
        final NPC[] npcs = Npcs.getNearest("Banker");
        if (npcs != null && npcs.length > 0) {
            final NPC n = npcs[0];
            if (n != null) {
                n.interact("Bank");
                Time.sleep(() -> Bank.isOpen(), Random.between(2500, 5000));
                return true;
            }
        } else if (objects != null && objects.length > 0) {
            final SceneObject o = objects[0];
            if (o != null) {
                o.interact("Bank");
                Time.sleep(() -> Bank.isOpen(), Random.between(2500, 5000));
                return true;
            }
        }
        return false;
    }

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

    public static int getCount(int id) {
        if (!isOpen()) {
            return 0;
        }
        Item item;

        return ((item = getItem(id)) != null ? item.getStackSize() : 0);
    }

    public static int getCount(String name) {
        if (!isOpen()) {
            return 0;
        }
        Item item;

        return ((item = getItem(name)) != null ? item.getStackSize() : 0);
    }

    public static boolean isOpen() {
        final InterfaceChild wc = Interfaces.get(12, 4);
        return wc != null && wc.isVisible();
    }

    public static boolean close() {
        if (!isOpen())
            return true;
        final InterfaceChild closeButton = Interfaces.get(12, 3).getChild(11);
        return closeButton.isVisible() && closeButton.click();
    }

    public static boolean depositAll() {
        if (!isOpen())
            return true;
        final InterfaceChild closeButton = Interfaces.get(12, 42);
        return closeButton.isVisible() && closeButton.click();
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

    public static Item getItem(Filter<Item> filter) {
        Item[] items = getItems(filter);

        if (items == null || items.length == 0)
            return null;

        return items[0];
    }

    public static Item[] getItems(final int... ids) {
        return getItems(e -> {
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
        return getItems(e -> {
            for (String name : names) {
                if (e.getName().toLowerCase().equals(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        });
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

    public static boolean withdraw(final String name, final int amount) {
        if (!isOpen())
            return false;
        final Item item = Bank.getItem(name);
        if (!item.isValid())
            return false;
        final int amountBeforeWithdraw = Bank.getCount(name);
        String action;
        for (int i = 0; i < 4 && Bank.getCount(name) == amountBeforeWithdraw; i++) {
            switch (amount) {
                case 1:
                    action = "Withdraw-1";
                    break;
                case 5:
                    action = "Withdraw-5";
                    break;
                case 10:
                    action = "Withdraw-10";
                    break;
                default:
                    action = "Withdraw-All";
                    break;
            }
            if (action.equals("Withdraw-X")) {
                Time.sleep(2500);
                //Gotta do that keybaord
            }
            item.interact(action);
            Time.sleep(() -> Bank.getCount(name) != amountBeforeWithdraw, 3000);
            if (Bank.getCount(name) != amountBeforeWithdraw) {
                break;
            } else {
                continue;
            }
        }
        return true;
    }

    public static boolean withdraw(final int id, final int amount) {
        if (!isOpen())
            return false;
        final Item item = Bank.getItem(id);
        if (!item.isValid())
            return false;
        final int amountBeforeWithdraw = Bank.getCount(id);
        String action;
        for (int i = 0; i < 4 && Bank.getCount(id) == amountBeforeWithdraw; i++) {
            switch (amount) {
                case 1:
                    action = "Withdraw-1";
                    break;
                case 5:
                    action = "Withdraw-5";
                    break;
                case 10:
                    action = "Withdraw-10";
                    break;
                default:
                    action = "Withdraw-All";
                    break;
            }
            if (action.equals("Withdraw-X")) {
                Time.sleep(2500);
                //Gotta do that keybaord
            }
            item.interact(action);
            Time.sleep(() -> Bank.getCount(id) != amountBeforeWithdraw, 3000);
            if (Bank.getCount(id) != amountBeforeWithdraw) {
                break;
            } else {
                continue;
            }
        }
        return true;
    }

    public static boolean deposit(final int id, final int amount) {
        if (!isOpen())
            return false;
        final Item item = getItem(id);
        if (!item.isValid())
            return false;
        final int invAmount = getCount(id);
        String action = "Deposit-X";
        if (amount >= invAmount) {
            action = "Deposit-All";
        } else {
            switch (invAmount) {
                case 1:
                    action = "Deposit-1";
                    break;
                case 5:
                    action = "Deposit-5";
                    break;
                case 10:
                    action = "Deposit-10";
                    break;
            }
        }
        item.interact(action);
        if (action.equals("Deposit-X")) {
            Time.sleep(2500);
            //Gotta do that keybaord
        }
        Time.sleep(() -> getCount(id) != invAmount, 3000);
        return true;
    }

    public static boolean depositAllExcept(final String... names) {
        return depositAllExcept(item -> item.isValid() && item.getName() != null
                && Utils.getInstance().inArray(item.getName(), names));
    }

    public static boolean depositAllExcept(final int... ids) {
        return depositAllExcept(item -> item.isValid() && Utils.getInstance().inArray(item.getId(), ids));
    }

    public static boolean depositAllExcept(Filter<Item> filter) {
        boolean deposit = false;
        for (final Item i : Inventory.getAllItems(i -> true)) {
            if (filter.accept(i)) {
                continue;
            }
            deposit(i.getId(), 999999999);
            deposit = true;
        }
        return deposit;
    }
}