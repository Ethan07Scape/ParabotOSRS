package org.osrs.min.loading.injectors;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.osrs.min.api.callbacks.PacketCallback;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;

import java.util.List;

public class PacketData {

    /**
     * Injects a callback to log outgoing packets.
     * <p>
     * TODO - Clean this class.
     *
     * @author Ethan
     */

    private final XMLHookParser xmlHookParser;

    public PacketData(ClassNode cn, XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        handleInjections(cn.methods);
    }

    public void handleInjections(List<MethodNode> methodNodes) {
        final List<MethodNode> methods = methodNodes;
        injectInsideMethod(xmlHookParser.getMethodByGetter("writeInt"), methods);
        injectInsideMethod(xmlHookParser.getMethodByGetter("writeShort"), methods);
        injectInsideMethod(xmlHookParser.getMethodByGetter("writeByte"), methods);

    }

    @SuppressWarnings("deprecation")
    private void injectInsideMethod(MethodFrame methodFrame, List<MethodNode> methods) {
        for (MethodNode mn : methods) {
            if (mn.name.equals(methodFrame.getMethodName())) {
                if (mn.desc.equals(methodFrame.getMethodDesc())) {
                    System.out.println("We're inside: " + methodFrame.getMethodGetter());
                    final InsnList nl = new InsnList();
                    boolean injected = false;
                    final AbstractInsnNode[] mnNodes = mn.instructions.toArray();
                    for (AbstractInsnNode abstractInsnNode : mnNodes) {
                        nl.add(abstractInsnNode);
                        if (!injected) {
                            if (abstractInsnNode.getOpcode() == Opcodes.BASTORE) {
                                nl.add(new VarInsnNode(Opcodes.ILOAD, 1));
                                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, PacketCallback.class.getCanonicalName().replace('.', '/'), "printString", "(" + "I" + ")V"));
                                System.out.println("Finished injecting inside: " + methodFrame.getMethodGetter());
                                injected = true;
                            }
                        }
                    }
                    mn.instructions = nl;
                    mn.visitMaxs(0, 0);
                    mn.visitEnd();
                }
            }
        }
    }
}
