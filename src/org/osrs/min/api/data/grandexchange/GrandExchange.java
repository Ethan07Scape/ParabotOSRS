package org.osrs.min.api.data.grandexchange;

import org.osrs.min.api.interactive.Interfaces;
import org.osrs.min.api.interactive.Npcs;
import org.osrs.min.api.wrappers.InterfaceChild;
import org.osrs.min.api.wrappers.NPC;

public class GrandExchange {

    private static int PARENT_INTERFACE = 465;

    public static boolean isOpen() {
        final InterfaceChild i = Interfaces.get(PARENT_INTERFACE, 2);
        return i != null && i.isVisible();
    }

    public static boolean open() {
        if (isOpen())
            return true;
        final NPC n = Npcs.getClosest("Grand Exchange Clerk");
        return n != null && n.interact("Exchange");
    }

}
