package org.osrs.min.loading.injectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.osrs.min.api.accessors.ObjectDefinition;
import org.osrs.min.loading.hooks.XMLHookParser;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.asm.ASMUtils;

public class Invokers {

    private final XMLHookParser xmlHookParser;

    public Invokers(XMLHookParser xmlHookParser) {
        this.xmlHookParser = xmlHookParser;
        addDefinition("getObjectDefinition", ObjectDefinition.class);
    }

    private final void addDefinition(String name, Class<?> clazz) {
        final MethodFrame invoker = xmlHookParser.getMethodByGetter(name);
        final ClassNode into = ASMUtils.getClass(xmlHookParser.getClassByAccessor(invoker.getIntoAccessor()));
        if (into != null) {
            System.out.println("We need to inject definiton into: " + into.name);
            addDefinitionMethod(into, name, invoker.getMethodDesc(), invoker.getMethodClass(), invoker.getMethodName(), "L" + clazz.getCanonicalName().replace('.', '/') + ";", invoker.getArgsDesc());
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
        System.out.println(mn.name + " - " + mn.desc);
        System.out.println("Definition added...");
    }
}
