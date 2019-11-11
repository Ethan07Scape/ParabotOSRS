package org.osrs.min.api.packets.outgoing;

public class PacketMeta {
    private org.osrs.min.api.accessors.ClientPacket accessor;

    public PacketMeta(Object accessor) {
        this.accessor = (org.osrs.min.api.accessors.ClientPacket) accessor;
    }

    public int getOpcode() {
        if (accessor == null)
            return -1;
        return this.accessor.getId();
    }

    public int getPacketSize() {
        if (accessor == null)
            return -1;
        return this.accessor.getLength();
    }

    public org.osrs.min.api.accessors.ClientPacket getAccessor() {
        return this.accessor;
    }
}
