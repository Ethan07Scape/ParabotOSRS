package org.osrs.min.loading.injectors;


import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.osrs.min.threads.FocusChanger;
import org.parabot.core.asm.ASMUtils;

import java.util.List;

public class FocusCallback {

    /**
     * Injects a callback to log the client's focus & grab the hook it sets.
     * <p>
     * TODO - Clean this class.
     *
     * @author Ethan
     */

    public FocusCallback() {
        handleInjections();
    }

    public void handleInjections() {
        final ClassNode classNode = ASMUtils.getClass("client");
        final ClassNode superClassNode = ASMUtils.getClass(classNode.superName);
        final List<MethodNode> methods = superClassNode.methods;
        injectInsideFocus(methods);
    }

    @SuppressWarnings("deprecation")
    private void injectInsideFocus(List<MethodNode> methods) {
        for (MethodNode mn : methods) {
            if (mn.name.equals("focusLost")) {
                final AbstractInsnNode[] mnNodes = mn.instructions.toArray();
                for (AbstractInsnNode abstractInsnNode : mnNodes) {
                    if (abstractInsnNode instanceof FieldInsnNode) {
                        final FieldInsnNode volatileFocus = (FieldInsnNode) abstractInsnNode;
                        System.out.println(volatileFocus.owner + "." + volatileFocus.name + "(" + volatileFocus.desc + ")");
                        final FocusChanger focusChanger = new FocusChanger();
                        focusChanger.setFocusClass(volatileFocus.owner);
                        focusChanger.setFocusField(volatileFocus.name);
                        focusChanger.start();
                    }
                }
            }
        }
    }
}

