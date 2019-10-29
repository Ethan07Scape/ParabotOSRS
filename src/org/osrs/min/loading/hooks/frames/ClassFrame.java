package org.osrs.min.loading.hooks.frames;

public class ClassFrame {
    private final String classGetter;
    private final String className;

    public ClassFrame(String classGetter, String className) {
        this.classGetter = classGetter;
        this.className = className;
    }

    public String getClassGetter() {
        return classGetter;
    }

    public String getClassName() {
        return className;
    }
}
