package org.example.model;
/**
 * 对比失败的字段信息
 */

public class FailedField {

    private String fieldName;
    private String errorMessage;
    private String errorType;

    public FailedField() {
    }

    public FailedField(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public FailedField(String fieldName, Exception e) {
        this.fieldName = fieldName;
        this.errorMessage = e.getMessage();
        this.errorType = e.getClass().getSimpleName();
    }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }

    @Override
    public String toString() {
        if (errorType != null) {
            return fieldName + ": [" + errorType + "] " + errorMessage;
        }
        return fieldName + ": " + errorMessage;
    }
}