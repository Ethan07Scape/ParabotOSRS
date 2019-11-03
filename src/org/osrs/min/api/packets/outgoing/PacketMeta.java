package org.osrs.min.api.packets.outgoing;

public class PacketMeta {
    private org.osrs.min.api.accessors.OutgoingPacketMeta accessor;

    public PacketMeta(Object accessor) {
        this.accessor = (org.osrs.min.api.accessors.OutgoingPacketMeta) accessor;
    }

    public int getOpcode() {
        if (accessor == null)
            return -1;
        return this.accessor.getOpcode();
    }

    public int getPacketSize() {
        if (accessor == null)
            return -1;
        return this.accessor.getSize();
    }

    public org.osrs.min.api.accessors.OutgoingPacketMeta getAccessor() {
        return this.accessor;
    }
}
