package org.osrs.min.api.data;

import org.osrs.min.api.packets.outgoing.PacketMeta;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class OutgoingPackets {

    public static List<PacketMeta> getPackets(Filter<PacketMeta> filter) {
        final List<PacketMeta> packets = new ArrayList<>();
        try {
            for (org.osrs.min.api.accessors.ClientPacket packet : Loader.getClient().getPackets()) {
                final PacketMeta packetMeta = new PacketMeta(packet);
                if (filter.accept(packetMeta))
                    packets.add(packetMeta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packets;
    }

    public static List<PacketMeta> getPackets() {
        return getPackets(o -> true);
    }

    public static PacketMeta getPacket(Filter<PacketMeta> filter) {
        final List<PacketMeta> packets = getPackets(filter);
        if (packets == null || packets.size() <= 0)
            return null;
        return getPackets(filter).get(0);
    }
}
