package org.osrs.min.api.packets.outgoing;


import org.osrs.min.api.data.Game;
import org.osrs.min.api.data.OutgoingPackets;
import org.osrs.min.loading.Loader;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.Context;

import java.lang.reflect.Method;
import java.util.Random;

public class PacketWriter {
    private static final int FOCUS_OPCODE_REV185 = 10;
    private static final int FOCUS_OPCODE_SIZE_REV185 = 1;

    private static final int WINDOW_DOWNSIZED_REV185_OPCODE = 76;
    private static final int WINDOW_DOWNSIZED_REV185_SIZE = 5;

    private static PacketWriter instance;
    private final XMLHookParser xmlHookParser;
    private final MethodFrame getBufferNode;
    private final MethodFrame writeLater;

    private PacketMeta packetMeta;
    private Object bufferObj;
    private PacketNode packetNode;
    private PacketBuffer packetBuffer;

    public PacketWriter(XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        getBufferNode = xmlHookParser.getMethodByGetter("getPacketBufferNode");
        writeLater = xmlHookParser.getMethodByGetter("addNode");
        instance = this;
    }

    public static PacketWriter getInstance() {
        if (instance == null)
            return null;
        return instance;
    }


    private Object getOutgoingPacketObj(Object... args) {
        if (getBufferNode == null)
            return null;
        try {
            final Class<?> c = Context.getInstance().getASMClassLoader().loadClass(getBufferNode.getMethodClass());
            final String returnClass = getBufferNode.getMethodDesc().split("\\)")[1].replaceFirst("L", "").replaceAll(";", "");
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(getBufferNode.getMethodName())) {
                    if (m.getGenericReturnType().getTypeName().equals(returnClass)) {
                        m.setAccessible(true);
                        return m.invoke(null, args);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Object addNode(Object instance, Object... args) {
        if (writeLater == null)
            return null;
        try {
            final Class<?> c = Context.getInstance().getASMClassLoader().loadClass(writeLater.getMethodClass());
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(writeLater.getMethodName())) {
                    if (m.getParameterCount() == 2) {
                        if (m.toGenericString().contains("public final void")) {
                            m.setAccessible(true);
                            return m.invoke(instance, args);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendRandomFocus() {
        if (!Game.isLoggedIn())
            return;
        if (!setUpPacket(FOCUS_OPCODE_REV185, FOCUS_OPCODE_SIZE_REV185))
            return;
        if (!isPacketSet())
            return;

        if (new Random().nextBoolean() == true) {
            System.out.println("Sending focus: LOST");
            packetBuffer.writeByte(0);
        } else {
            System.out.println("Sending focus: GAINED");
            packetBuffer.writeByte(1);
        }

        final byte access = (byte) 0;
        addNode(Loader.getClient().getPacketWriter(), packetNode.getAccessor(), access);

    }

    public void sendRandomDownsize() {
        if (!Game.isLoggedIn())
            return;
        if (!setUpPacket(WINDOW_DOWNSIZED_REV185_OPCODE, WINDOW_DOWNSIZED_REV185_SIZE))
            return;
        if (!isPacketSet())
            return;

        System.out.println("Sending downsize event!");

        packetBuffer.writeByte(0);
        packetBuffer.writeShort(org.parabot.api.calculations.Random.between(700, 1200)); //clientWidth
        packetBuffer.writeShort(org.parabot.api.calculations.Random.between(500, 1200)); //clientHeight

        final byte access = (byte) 0;
        addNode(Loader.getClient().getPacketWriter(), packetNode.getAccessor(), access);

    }

    public boolean setUpPacket(int opcode, int size) {
        packetMeta = OutgoingPackets.getPacket(p -> p.getOpcode() == opcode && p.getPacketSize() == size);
        if (packetMeta == null) {
            System.out.println("no packetmeta");
            return false;
        }

        bufferObj = getOutgoingPacketObj(packetMeta.getAccessor(), Loader.getClient().getPacketWriter().getIsaacCipher(), XMLHookParser.getDummyValue("getPacketBufferNode"));
        if (bufferObj == null) {
            System.out.println("no buffernode");
            return false;
        }

        packetNode = new PacketNode((org.osrs.min.api.accessors.PacketBufferNode) bufferObj);
        if (packetNode == null) {
            System.out.println("no packetnode");
            return false;
        }

        packetBuffer = packetNode.getBuffer();
        if (packetBuffer == null) {
            System.out.println("no pbuffer");
        }
        return packetBuffer != null;
    }

    public boolean isPacketSet() {
        if (packetMeta == null)
            return false;

        if (bufferObj == null)
            return false;

        if (packetNode == null)
            return false;

        return packetBuffer != null;
    }
}
