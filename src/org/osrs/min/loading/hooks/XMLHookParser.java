package org.osrs.min.loading.hooks;

import org.osrs.min.loading.hooks.frames.ClassFrame;
import org.osrs.min.loading.hooks.frames.FieldFrame;
import org.osrs.min.loading.hooks.frames.MethodFrame;
import org.parabot.core.Core;
import org.parabot.core.asm.hooks.HookFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses an XML file full of hookers
 * <p>
 * TODO - Clean this class.
 *
 * @author JKetelaar, Ethan
 */

public class XMLHookParser {
    private Document doc;
    private List<ClassFrame> classes;
    private static Map<ClassFrame, List<FieldFrame>> fieldMap;
    private static Map<ClassFrame, List<MethodFrame>> methodMap;

    public XMLHookParser(HookFile hookFile) {
        classes = new ArrayList<>();
        fieldMap = new HashMap<>();
        methodMap = new HashMap<>();
        try {
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(hookFile.getInputStream(null));
            doc.getDocumentElement().normalize();
            if (!doc.getDocumentElement().getNodeName().equals("injector")) {
                throw new RuntimeException("Incorrect hook file.");
            }
        } catch (Throwable t) {
            throw new RuntimeException("Unable to parse hooks " + t);
        }
        populateInterfaces();
        populateFields();
        populateMethods();
    }

    public static void main(String[] args) {

    }

    private void populateInterfaces() {
        final NodeList interfaceRootList = doc
                .getElementsByTagName("interfaces");
        switch (interfaceRootList.getLength()) {
            case 0:
                return;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <interfaces> tags ");
        }
        final Node node = interfaceRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        final Element interfaceRoot = (Element) node;
        final NodeList interfaces = interfaceRoot.getElementsByTagName("add");
        if (interfaces.getLength() == 0) {
            return;
        }
        for (int x = 0; x < interfaces.getLength(); x++) {
            final Node n = interfaces.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addInterface = (Element) n;
            final String className = getValue("classname", addInterface);
            final String interfaceClass = getValue("interface", addInterface);
            classes.add(new ClassFrame(interfaceClass, className));
            fieldMap.putIfAbsent(new ClassFrame(interfaceClass, className), new ArrayList<>());
            methodMap.putIfAbsent(new ClassFrame(interfaceClass, className), new ArrayList<>());
        }
    }

    private void populateFields() {
        final NodeList getterRootList = doc.getElementsByTagName("getters");
        switch (getterRootList.getLength()) {
            case 0:
                return;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <getters> tags ");
        }
        final Node node = getterRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        final Element getterRoot = (Element) node;
        final NodeList getters = getterRoot.getElementsByTagName("add");
        if (getters.getLength() == 0) {
            return;
        }
        for (int x = 0; x < getters.getLength(); x++) {
            final Node n = getters.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addGetter = (Element) n;
            if (isSet("classname", addGetter) && isSet("accessor", addGetter)) {
                throw new RuntimeException(
                        "Can't set classname and accessor tag together.");
            }
            String accessor = null;
            for (Map.Entry<ClassFrame, List<FieldFrame>> entry : fieldMap.entrySet()) {
                final ClassFrame frame = entry.getKey();
                String value = getValue("accessor", addGetter);
                if (value != null) {
                    if (frame.getClassGetter().equals(value)) {
                        final long multiplier = isSet("multiplier", addGetter) ? Long.parseLong(getValue("multiplier", addGetter)) : 0L;
                        final String fieldName = getValue("field", addGetter);
                        final String methodName = getValue("methodname", addGetter);
                        entry.getValue().add(new FieldFrame(methodName, fieldName, String.valueOf(multiplier)));
                    }
                } else {
                    final String into = getValue("into", addGetter);
                    if (into != null && into.length() > 0 && into.toLowerCase().equals("client")) {
                        final long multiplier = isSet("multiplier", addGetter) ? Long.parseLong(getValue("multiplier", addGetter)) : 0L;
                        final String fieldName = getValue("field", addGetter);
                        final String methodName = getValue("methodname", addGetter);
                        entry.getValue().add(new FieldFrame(methodName, fieldName, String.valueOf(multiplier)));
                    }
                }
            }
        }
    }

    public static int getDummyValue(String getter) {
        for (Map.Entry<ClassFrame, List<MethodFrame>> entry : methodMap.entrySet()) {
            for (MethodFrame m : entry.getValue()) {
                if (m.getMethodGetter().toLowerCase().equals(getter.toLowerCase())) {
                    return m.getDummyValue();
                }
            }
        }
        return -1;
    }

    private final boolean isSet(String tag, Element element) {
        return element.getElementsByTagName(tag).getLength() > 0;
    }

    private final String getValue(String tag, Element element) {
        if (element.getElementsByTagName(tag).item(0) == null) {
            return null;
        }
        NodeList nodes = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        if (nodes.getLength() == 0 || nodes.item(0) == null) {
            if (Core.inVerboseMode()) {
                System.err.println("WARNING: Invalid Hook " + tag + " subnode. Tag is missing or empty?");
            }
            return "";
        }
        Node node = nodes.item(0);
        return node.getNodeValue();
    }

    public void populateMethods() {
        final NodeList invokerRootList = doc.getElementsByTagName("custominvokers");
        switch (invokerRootList.getLength()) {
            case 0:
                return;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <invokers> tags ");
        }
        final Node node = invokerRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        final Element invokerRoot = (Element) node;
        final NodeList invokers = invokerRoot.getElementsByTagName("add");
        if (invokers.getLength() == 0) {
            return;
        }
        for (int x = 0; x < invokers.getLength(); x++) {
            final Node n = invokers.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addInvoker = (Element) n;

            final String className = getValue("classname", addInvoker);
            final String into = getValue("accessor", addInvoker);
            final String methodName = getValue("methodname", addInvoker);
            final String invMethodName = getValue("invokemethod", addInvoker);
            final String desc = getValue("desc", addInvoker);
            final String argsDesc = getValue("argsdesc", addInvoker);
            final String dummyValue = getValue("dummyvalue", addInvoker);
            for (Map.Entry<ClassFrame, List<MethodFrame>> entry : methodMap.entrySet()) {
                final ClassFrame frame = entry.getKey();
                if (frame.getClassGetter().equals(into)) {
                    entry.getValue().add(new MethodFrame(methodName, className, invMethodName, desc, argsDesc, into, Integer.parseInt(dummyValue)));
                }
            }
        }
    }

    public String getClassByAccessor(String accessor) {
        for (ClassFrame frame : classes) {
            if (frame.getClassGetter().toLowerCase().equals(accessor.toLowerCase())) {
                return frame.getClassName();
            }
        }
        return null;
    }

    public MethodFrame getMethodByGetter(String getter) {
        for (Map.Entry<ClassFrame, List<MethodFrame>> entry : methodMap.entrySet()) {
            for (MethodFrame m : entry.getValue()) {
                if (m.getMethodGetter().toLowerCase().equals(getter.toLowerCase())) {
                    return m;
                }
            }
        }
        return null;
    }

    public FieldFrame getFieldByGetter(String getter) {
        for (Map.Entry<ClassFrame, List<FieldFrame>> entry : fieldMap.entrySet()) {
            for (FieldFrame f : entry.getValue()) {
                if (f.getFieldGetter().toLowerCase().equals(getter.toLowerCase())) {
                    return f;
                }
            }
        }
        return null;
    }

    public MethodFrame getMethodByAccessorAndGetter(String accessor, String getter) {
        for (Map.Entry<ClassFrame, List<MethodFrame>> entry : methodMap.entrySet()) {
            final ClassFrame classFrame = entry.getKey();
            if (classFrame.getClassGetter().toLowerCase().equals(accessor.toLowerCase())) {
                for (MethodFrame m : entry.getValue()) {
                    if (m.getMethodGetter().toLowerCase().equals(getter.toLowerCase())) {
                        return m;
                    }
                }
            }
        }
        return null;
    }
}
