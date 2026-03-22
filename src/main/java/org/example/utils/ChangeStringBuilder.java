package org.example.utils;

import org.example.enums.OperationType;
import org.example.model.FieldChange;

import java.util.ArrayList;
import java.util.List;

/**
 * 变更字符串构建器 - 负责将变更列表拼接成字符串
 */
class ChangeStringBuilder {

    static String build(List<FieldChange> changes, OperationType operationType) {
        if (changes == null || changes.isEmpty()) return "";

        List<String> changeStrings = new ArrayList<>();
        for (FieldChange change : changes) {
            String formatted = formatChange(change, operationType);
            if (!formatted.isEmpty()) {
                changeStrings.add(formatted);
            }
        }
        return String.join(", ", changeStrings);
    }

    private static String formatChange(FieldChange change, OperationType operationType) {
        String fieldName = change.getFieldName();
        Object oldValue = change.getOldValue();
        Object newValue = change.getNewValue();

        switch (operationType) {
            case ADD:
                if (newValue != null) {
                    return fieldName + ": " + ValueFormatter.formatValue(newValue);
                }
                break;
            case DELETE:
                if (oldValue != null) {
                    return fieldName + ": " + ValueFormatter.formatValue(oldValue);
                }
                break;
            case UPDATE:
                if (oldValue == null && newValue != null) {
                    return fieldName + ": " + ValueFormatter.formatValue(newValue);
                }
                if (oldValue != null && newValue == null) {
                    return fieldName + ": 删除(" + ValueFormatter.formatValue(oldValue) + ")";
                }
                if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                    return fieldName + ": " + ValueFormatter.formatValue(oldValue)
                            + "→" + ValueFormatter.formatValue(newValue);
                }
                break;
        }
        return "";
    }
}