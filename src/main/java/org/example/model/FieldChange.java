package org.example.model;

import java.util.Objects;


import java.util.Objects;

public class FieldChange {

    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private Class<?> fieldType;

    public FieldChange() {
    }

    public FieldChange(String fieldName, Object oldValue, Object newValue) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public FieldChange(String fieldName, Object oldValue, Object newValue, Class<?> fieldType) {
        this(fieldName, oldValue, newValue);
        this.fieldType = fieldType;
    }

    public boolean hasChanged() {
        return !Objects.equals(oldValue, newValue);
    }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public Object getOldValue() { return oldValue; }
    public void setOldValue(Object oldValue) { this.oldValue = oldValue; }
    public Object getNewValue() { return newValue; }
    public void setNewValue(Object newValue) { this.newValue = newValue; }
    public Class<?> getFieldType() { return fieldType; }
    public void setFieldType(Class<?> fieldType) { this.fieldType = fieldType; }
}