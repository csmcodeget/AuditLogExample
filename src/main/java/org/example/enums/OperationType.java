package org.example.enums;
/**
 * 操作类型枚举
 */
public enum OperationType {
    ADD("新增"),
    UPDATE("更新"),
    DELETE("删除");

    private String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return this.name();
    }
}