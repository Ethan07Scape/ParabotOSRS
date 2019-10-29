package org.osrs.min.loading.hooks.frames;

public class FieldFrame {
    private final String fieldGetter;
    private final String fieldName;
    private final String fieldMult;

    public FieldFrame(String fieldGetter, String fieldName, String fieldMult) {
        this.fieldGetter = fieldGetter;
        this.fieldName = fieldName;
        this.fieldMult = fieldMult;
    }

    public String getFieldGetter() {
        return fieldGetter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldMult() {
        return fieldMult;
    }
}
