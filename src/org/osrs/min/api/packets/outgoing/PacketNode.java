package org.osrs.min.api.packets.outgoing;

public class PacketNode {

    private final org.osrs.min.api.accessors.PacketBufferNode accessor;

    public PacketNode(org.osrs.min.api.accessors.PacketBufferNode accessor) {
        this.accessor = accessor;
    }

    private org.osrs.min.api.accessors.PacketBuffer getPacketBuffer() {
        if (getAccessor() == null)
            return null;
        return this.accessor.getPacketBuffer();
    }

    public PacketBuffer getBuffer() {
        if (getAccessor() == null)
            return null;
        if (getPacketBuffer() == null)
            return null;
        return new PacketBuffer((org.osrs.min.api.accessors.Buffer) getPacketBuffer());
    }

    public org.osrs.min.api.accessors.PacketBufferNode getAccessor() {
        return this.accessor;
    }
}
