package org.osrs.min.loading.hooks.frames;

public class MethodFrame {
    private final String methodGetter;
    private final String methodClass;
    private final String methodName;
    private final String methodDesc;
    private final String intoAccessor;

    public MethodFrame(String methodGetter, String methodClass, String methodName, String methodDesc, String intoAccessor) {
        this.methodGetter = methodGetter;
        this.methodClass = methodClass;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.intoAccessor = intoAccessor;
    }

    public String getMethodGetter() {
        return methodGetter;
    }

    public String getMethodClass() {
        return methodClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getIntoAccessor() {
        return intoAccessor;
    }
}
