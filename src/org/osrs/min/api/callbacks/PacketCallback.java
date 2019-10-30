package org.osrs.min.api.callbacks;

import org.osrs.min.api.packet.OutgoingPacketMeta;

public class PacketCallback {
    private static boolean debug = false;

    public static void printOpcode(Object object) {
        if (debug) {
            final OutgoingPacketMeta outgoingPacketMeta = new OutgoingPacketMeta(object);
            System.out.println("Op: " + outgoingPacketMeta.getOpcode() + " - Size: " + outgoingPacketMeta.getPacketSize());
        }
    }
}
