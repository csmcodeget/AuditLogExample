package org.example.utils;

/**
 * 值格式化器 - 负责格式化单个值
 */
public class ValueFormatter {

    private static final int MAX_FIELD_VALUE_LENGTH = 500;
    private static final int MAX_DATA_LENGTH = 2000;

    static String formatValue(Object value) {
        if (value == null) return "";
        String str = value.toString();
        if (str.length() > MAX_FIELD_VALUE_LENGTH) {
            return str.substring(0, MAX_FIELD_VALUE_LENGTH) + "...";
        }
        return str;
    }

    static String truncate(String data) {
        if (data == null) return "";
        if (data.length() > MAX_DATA_LENGTH) {
            return data.substring(0, MAX_DATA_LENGTH) + "...(已截断)";
        }
        return data;
    }
}