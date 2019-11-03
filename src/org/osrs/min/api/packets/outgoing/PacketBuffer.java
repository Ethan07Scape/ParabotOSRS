package org.osrs.min.api.packets.outgoing;

public class PacketBuffer {

    private org.osrs.min.api.accessors.Buffer accessor;

    public PacketBuffer(org.osrs.min.api.accessors.Buffer accessor) {
        this.accessor = accessor;
    }

    public void writeByte(int i) {
        this.accessor.writeByte(i, 0);
    }

    public void writeShort(int i) {
        this.accessor.writeShort(i, 0);
    }

    public org.osrs.min.api.accessors.Buffer getAccessor() {
        return this.accessor;
    }
}
