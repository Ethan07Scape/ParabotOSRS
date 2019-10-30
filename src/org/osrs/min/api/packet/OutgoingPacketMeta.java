package org.osrs.min.api.packet;

public class OutgoingPacketMeta {
    private org.osrs.min.api.accessors.OutgoingPacketMeta accessor;

    public OutgoingPacketMeta(Object accessor) {
        this.accessor = (org.osrs.min.api.accessors.OutgoingPacketMeta) accessor;
    }

    public int getOpcode() {
        return this.accessor.getOpcode();
    }

    public int getPacketSize() {
        return this.accessor.getSize();
    }

    protected org.osrs.min.api.accessors.OutgoingPacketMeta getAccessor() {
        return this.accessor;
    }
}
