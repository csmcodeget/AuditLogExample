package org.example.utils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 简单审计日志工具类
 * 功能：
 * 1. 支持父类字段
 * 2. 支持 includes 过滤（Set 和可变参数）
 * 3. 自动截断长文本
 * 4. 异常安全
 */
public class SimpleAuditUtil {

    // ==================== 新增日志 ====================

    /**
     * 获取新增数据（所有非空字段）
     */
    public static String getAddData(Object entity) {
        return getAddData(entity, (Set<String>) null);
    }

    /**
     * 获取新增数据（只包含指定字段）- 可变参数
     */
    public static String getAddData(Object entity, String... includeFields) {
        return getAddData(entity, toSet(includeFields));
    }

    /**
     * 获取新增数据（只包含指定字段）- Set
     */
    public static String getAddData(Object entity, Set<String> includeFields) {
        if (entity == null) return "";

        Map<String, Object> data = new LinkedHashMap<>();
        for (Field field : getAllFields(entity.getClass())) {
            String fieldName = field.getName();

            // 如果有 includes 过滤，只处理指定字段
            if (includeFields != null && !includeFields.isEmpty() && !includeFields.contains(fieldName)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    data.put(fieldName, formatValue(value));
                }
            } catch (IllegalAccessException e) {
                // 忽略反射异常，不影响主流程
            }
        }
        return data.toString();
    }

    // ==================== 删除日志 ====================

    /**
     * 获取删除数据（所有非空字段）
     */
    public static String getDeleteData(Object entity) {
        return getDeleteData(entity, (Set<String>) null);
    }

    /**
     * 获取删除数据（只包含指定字段）- 可变参数
     */
    public static String getDeleteData(Object entity, String... includeFields) {
        return getDeleteData(entity, toSet(includeFields));
    }

    /**
     * 获取删除数据（只包含指定字段）- Set
     */
    public static String getDeleteData(Object entity, Set<String> includeFields) {
        return getAddData(entity, includeFields);
    }

    // ==================== 更新日志 ====================

    /**
     * 获取更新数据（所有变更字段）
     */
    public static String getUpdateData(Object oldObj, Object newObj) {
        return getUpdateData(oldObj, newObj, (Set<String>) null);
    }

    /**
     * 获取更新数据（只包含指定字段的变更）- 可变参数
     */
    public static String getUpdateData(Object oldObj, Object newObj, String... includeFields) {
        return getUpdateData(oldObj, newObj, toSet(includeFields));
    }

    /**
     * 获取更新数据（只包含指定字段的变更）- Set
     */
    public static String getUpdateData(Object oldObj, Object newObj, Set<String> includeFields) {
        if (oldObj == null || newObj == null) return "";

        Map<String, String> changes = new LinkedHashMap<>();
        for (Field field : getAllFields(oldObj.getClass())) {
            String fieldName = field.getName();

            // 如果有 includes 过滤，只处理指定字段
            if (includeFields != null && !includeFields.isEmpty() && !includeFields.contains(fieldName)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object oldVal = field.get(oldObj);
                Object newVal = field.get(newObj);
                if (!Objects.equals(oldVal, newVal)) {
                    changes.put(fieldName, formatChange(oldVal, newVal));
                }
            } catch (IllegalAccessException e) {
                // 忽略反射异常
            }
        }
        return changes.toString();
    }

    // ==================== 通用方法 ====================

    /**
     * 通用方法：自动判断操作类型
     */
    public static String getChangeData(Object oldObj, Object newObj) {
        return getChangeData(oldObj, newObj, (Set<String>) null);
    }

    /**
     * 通用方法：自动判断操作类型（带 includes）
     */
    public static String getChangeData(Object oldObj, Object newObj, String... includeFields) {
        return getChangeData(oldObj, newObj, toSet(includeFields));
    }

    /**
     * 通用方法：自动判断操作类型（带 includes Set）
     */
    public static String getChangeData(Object oldObj, Object newObj, Set<String> includeFields) {
        if (oldObj == null && newObj != null) {
            return getAddData(newObj, includeFields);
        } else if (oldObj != null && newObj == null) {
            return getDeleteData(oldObj, includeFields);
        } else if (oldObj != null && newObj != null) {
            return getUpdateData(oldObj, newObj, includeFields);
        }
        return "";
    }

    // ==================== 私有方法 ====================

    /**
     * 获取所有字段（包括父类，排除静态和final字段）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                int mod = field.getModifiers();
                // 排除静态字段和final字段
                if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    fields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * 将可变参数转为 Set
     */
    private static Set<String> toSet(String[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        return new HashSet<>(Arrays.asList(arr));
    }

    /**
     * 格式化值（防止过长）
     */
    private static String formatValue(Object value) {
        if (value == null) return "";
        String str = value.toString();
        int maxLen = 500;
        if (str.length() > maxLen) {
            return str.substring(0, maxLen) + "...";
        }
        return str;
    }

    /**
     * 格式化变更值
     */
    private static String formatChange(Object oldVal, Object newVal) {
        String oldStr = oldVal != null ? formatValue(oldVal) : "null";
        String newStr = newVal != null ? formatValue(newVal) : "null";

        // 如果值相同，返回空（理论上不会走到这里，但做个保护）
        if (oldStr.equals(newStr)) {
            return "";
        }

        return oldStr + "→" + newStr;
    }
}
