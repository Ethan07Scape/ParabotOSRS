package org.osrs.min.loading.hooks.frames;

public class MethodFrame {
    private final String methodGetter;
    private final String methodClass;
    private final String methodName;
    private final String methodDesc;
    private final String argsDesc;
    private final String intoAccessor;
    private final int dummyValue;

    public MethodFrame(String methodGetter, String methodClass, String methodName, String methodDesc, String argsDesc, String intoAccessor, int dummyValue) {
        this.methodGetter = methodGetter;
        this.methodClass = methodClass;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.argsDesc = argsDesc;
        this.intoAccessor = intoAccessor;
        this.dummyValue = dummyValue;
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

    public int getDummyValue() {
        return dummyValue;
    }

    public String getArgsDesc() {
        return argsDesc;
    }
}
