package org.osrs.min.loading.injectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.osrs.min.api.accessors.ItemDefinition;
import org.osrs.min.api.accessors.Node;
import org.osrs.min.api.accessors.ObjectDefinition;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.asm.ASMUtils;

public class Invokers {

    private final XMLHookParser xmlHookParser;

    public Invokers(XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        addDefinition("getObjectDefinition", ObjectDefinition.class);
        addDefinition("ItemDefinition_get", ItemDefinition.class);
        addInvoker("NodeDeque", "next", Node.class);
        addInvoker("NodeDeque", "last", Node.class);
        addInvoker("NodeDeque", "previous", Node.class);

        addNodes("writeInt", "writeByte", "writeLEInt", "writeByteA", "writeShortA", "writeByteS", "writeShort", "writeLEShort");
    }

    private final void addDefinition(String name, Class<?> clazz) {
        final MethodFrame invoker = xmlHookParser.getMethodByGetter(name);
        if (invoker == null)
            return;
        final ClassNode into = ASMUtils.getClass(xmlHookParser.getClassByAccessor(invoker.getIntoAccessor()));
        if (into != null) {
            addDefinitionMethod(into, name, invoker.getMethodDesc(), invoker.getMethodClass(), invoker.getMethodName(), "L" + clazz.getCanonicalName().replace('.', '/') + ";", invoker.getArgsDesc());
        }
    }

    private final void addInvoker(String accessor, String name, Class<?> clazz) {
        final MethodFrame invoker = xmlHookParser.getMethodByAccessorAndGetter(accessor, name);
        if (invoker == null) {
            return;
        }
        final ClassNode into = ASMUtils.getClass(xmlHookParser.getClassByAccessor(invoker.getIntoAccessor()));
        if (into != null) {
            System.out.println(into + " - " + name + " - " + invoker.getMethodDesc() + " - " + invoker.getMethodClass() + " - " + invoker.getMethodName());
            addInvoker(into, name, invoker.getMethodDesc(), invoker.getMethodClass(), invoker.getMethodName(), "L" + clazz.getCanonicalName().replace('.', '/') + ";", invoker.getArgsDesc());
        }
    }


    private final void addNodes(String... names) {
        PacketData packetData = null;
        for (String name : names) {
            final MethodFrame invoker = xmlHookParser.getMethodByGetter(name);

            if (invoker == null)
                return;

            final ClassNode into = ASMUtils.getClass(xmlHookParser.getClassByAccessor(invoker.getIntoAccessor()));
            if (packetData == null)
                packetData = new PacketData(into, xmlHookParser);
            if (into != null) {
                addNodeInvoke(into, name, invoker.getMethodDesc(), invoker.getMethodClass(), invoker.getMethodName(), invoker.getArgsDesc());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private final void addDefinitionMethod(ClassNode cn, final String name, final String desc, final String owner, final String mName, final String interfaceName, final String argsDesc) {
        final MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, name, argsDesc + interfaceName, null, null);
        mn.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
        mn.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
        mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, mName, desc));
        mn.instructions.add(new InsnNode(Opcodes.ARETURN));
        mn.visitMaxs(0, 0);
        mn.visitEnd();
        cn.methods.add(mn);
        System.out.println(interfaceName + " - Added...");
    }


    @SuppressWarnings("deprecation")
    private final void addInvoker(ClassNode cn, final String name, final String desc, final String owner, final String mName, final String interfaceName, final String argsDesc) {
        final MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, name, argsDesc + interfaceName, null, null);
        mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, mName, desc));
        mn.instructions.add(new InsnNode(Opcodes.ARETURN));
        mn.visitMaxs(0, 0);
        mn.visitEnd();
        cn.methods.add(mn);
        System.out.println(interfaceName + " - Added...");
    }

    @SuppressWarnings("deprecation")
    private final void addNodeInvoke(ClassNode cn, final String name, final String desc, final String owner, final String mName, final String argsDesc) {
        final MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, name, argsDesc + "V", null, null);
        mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        mn.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
        mn.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));

        mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, mName, desc));
        mn.instructions.add(new InsnNode(Opcodes.RETURN));
        mn.visitMaxs(0, 0);
        mn.visitEnd();
        cn.methods.add(mn);
        System.out.println(name + " - Added...");
    }
}
