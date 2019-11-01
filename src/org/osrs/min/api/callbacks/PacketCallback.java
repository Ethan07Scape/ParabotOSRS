package org.osrs.min.api.callbacks;

import org.osrs.min.api.packet.OutgoingPacketMeta;

public class PacketCallback {

    private static boolean debug = true;

    public static void printOpcode(Object object) {
        if (debug) {
            final OutgoingPacketMeta outgoingPacketMeta = new OutgoingPacketMeta(object);
            System.out.println("Op: " + outgoingPacketMeta.getOpcode() + " - Size: " + outgoingPacketMeta.getPacketSize());
        }
    }

    //40, -2 //key pressed
    //72, 5 //re-opened client
    //32, -1 //we talked
    //4 pinging server
}
