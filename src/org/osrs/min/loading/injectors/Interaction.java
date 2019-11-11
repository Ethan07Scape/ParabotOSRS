package org.osrs.min.loading.injectors;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.osrs.min.api.callbacks.ActionCallback;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interaction.MenuAction;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.asm.ASMUtils;

import java.util.List;

public class Interaction {

    /**
     * Injects our next game action into processAction
     * <p>
     * TODO - Clean this class.
     *
     * @author Ethan
     */

    private final XMLHookParser xmlHookParser;
    private final String methodGetter = "menuAction";
    private final MethodFrame method;

    public Interaction(XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        this.method = xmlHookParser.getMethodByGetter(methodGetter);
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
                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Game.class.getCanonicalName().replace('.', '/'), "isForcingAction", "()Z"));

                            LabelNode l3 = new LabelNode();
                            nl.add(new JumpInsnNode(Opcodes.IFEQ, l3));

                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InteractionHandler.class.getCanonicalName().replace('.', '/'), "getNextInteraction", "()L" + MenuAction.class.getCanonicalName().replace('.', '/') + ";"));
                            nl.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MenuAction.class.getCanonicalName().replace('.', '/'), "getSecondaryArg", "()I"));
                            nl.add(new VarInsnNode(Opcodes.ISTORE, 0));

                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InteractionHandler.class.getCanonicalName().replace('.', '/'), "getNextInteraction", "()L" + MenuAction.class.getCanonicalName().replace('.', '/') + ";"));
                            nl.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MenuAction.class.getCanonicalName().replace('.', '/'), "getTertiaryArg", "()I"));
                            nl.add(new VarInsnNode(Opcodes.ISTORE, 1));

                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InteractionHandler.class.getCanonicalName().replace('.', '/'), "getNextInteraction", "()L" + MenuAction.class.getCanonicalName().replace('.', '/') + ";"));
                            nl.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MenuAction.class.getCanonicalName().replace('.', '/'), "getOpcode", "()I"));
                            nl.add(new VarInsnNode(Opcodes.ISTORE, 2));

                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InteractionHandler.class.getCanonicalName().replace('.', '/'), "getNextInteraction", "()L" + MenuAction.class.getCanonicalName().replace('.', '/') + ";"));
                            nl.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MenuAction.class.getCanonicalName().replace('.', '/'), "getPrimaryArg", "()I"));
                            nl.add(new VarInsnNode(Opcodes.ISTORE, 3));

                            nl.add(l3);
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 0));
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 1));
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 2));
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 3));
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 6));
                            nl.add(new VarInsnNode(Opcodes.ILOAD, 7));
                            nl.add(new VarInsnNode(Opcodes.ALOAD, 5));
                            nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ActionCallback.class.getCanonicalName().replace('.', '/'), "add", "(" + "IIIIIILjava/lang/String;" + ")V"));

                            System.out.println("Finished injecting inside processAction");
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
