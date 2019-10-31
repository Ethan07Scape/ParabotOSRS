package org.osrs.min.loading.injectors;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.osrs.min.api.callbacks.PacketCallback;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.asm.ASMUtils;

import java.util.List;

public class PacketMeta {

    /**
     * Injects a callback to log outgoing packets.
     * <p>
     * TODO - Clean this class.
     *
     * @author Ethan
     */

    private final XMLHookParser xmlHookParser;
    private final MethodFrame method;

    public PacketMeta(XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        this.method = xmlHookParser.getMethodByGetter("getPacketBufferMeta");
        handleInjections();
    }

    public void handleInjections() {
        if (method == null)
            return;
        final ClassNode classNode = ASMUtils.getClass(method.getMethodClass());
        final List<MethodNode> methods = classNode.methods;
        injectInsideProcessAction(methods);
    }

    @SuppressWarnings("deprecation")
    private void injectInsideProcessAction(List<MethodNode> methods) {
        for (MethodNode mn : methods) {
            if (mn.name.equals(method.getMethodName())) {
                if (mn.desc.equals(method.getMethodDesc())) {
                    final InsnList nl = new InsnList();
                    boolean injected = false;
                    final AbstractInsnNode[] mnNodes = mn.instructions.toArray();
                    for (AbstractInsnNode abstractInsnNode : mnNodes) {
                        nl.add(abstractInsnNode);
                        if (!injected) {
                            nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, PacketCallback.class.getCanonicalName().replace('.', '/'), "printOpcode", "(" + "Ljava/lang/Object;" + ")V"));
                            System.out.println("Finished injecting inside PacketBuffer");
                            injected = true;
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
