package org.osrs.min.api.callbacks;

import org.osrs.min.api.packets.outgoing.PacketMeta;

public class PacketCallback {

    private static boolean DEBUG_OPCODES = true;
    private static boolean DEBUG_DATA = false;

    public static void printOpcode(Object object) {
        if (DEBUG_OPCODES) {
            final PacketMeta packetMeta = new PacketMeta(object);
            System.out.println("Op: " + packetMeta.getOpcode() + " - Size: " + packetMeta.getPacketSize());
        }
    }

    public static void printString(int object) {
        if (DEBUG_DATA) {
            System.out.println("PacketData: " + object);
        }
    }
    //40, -2 //key pressed
    //76 - Size: 5
    //32, -1 //we talked
    //4 pinging server
    /**
     Op: 76 - Size: 5
     PacketData: 1
     PacketData: 765
     PacketData: 503
     */
}
