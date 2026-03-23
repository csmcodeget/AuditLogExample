package org.example.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 审计日志工具 - 泛型版本
 */
public class AuditUtil {

    // ==================== 构建记录（新增/删除） ====================

    /**
     * 构建实体记录（所有非空字段）
     */
    public static <T> String buildRecord(T entity) {
        return buildRecord(entity, (Set<String>) null);
    }

    /**
     * 构建实体记录（只包含指定字段）- 可变参数
     */
    @SafeVarargs
    public static <T> String buildRecord(T entity, String... includeFields) {
        return buildRecord(entity, toSet(includeFields));
    }

    /**
     * 构建实体记录（只包含指定字段）- Set
     */
    public static <T> String buildRecord(T entity, Set<String> includeFields) {
        if (entity == null) return "";
        Map<String, Object> snapshot = new LinkedHashMap<>();
        for (Field field : getAllFields(entity.getClass())) {
            String fieldName = field.getName();
            if (includeFields != null && !includeFields.isEmpty() && !includeFields.contains(fieldName)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    snapshot.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
        return snapshot.toString();
    }

    // ==================== 构建变更（更新） ====================

    /**
     * 构建变更记录（所有变更字段）
     */
    public static <T> String buildChanges(T oldObj, T newObj) {
        return buildChanges(oldObj, newObj, (Set<String>) null);
    }

    /**
     * 构建变更记录（只包含指定字段的变更）- 可变参数
     */
    @SafeVarargs
    public static <T> String buildChanges(T oldObj, T newObj, String... includeFields) {
        return buildChanges(oldObj, newObj, toSet(includeFields));
    }

    /**
     * 构建变更记录（只包含指定字段的变更）- Set
     */
    public static <T> String buildChanges(T oldObj, T newObj, Set<String> includeFields) {
        if (oldObj == null || newObj == null) return "";
        Map<String, String> changes = new LinkedHashMap<>();
        for (Field field : getAllFields(oldObj.getClass())) {
            String name = field.getName();
            if (includeFields != null && !includeFields.isEmpty() && !includeFields.contains(name)) continue;
            field.setAccessible(true);
            try {
                Object oldVal = field.get(oldObj);
                Object newVal = field.get(newObj);
                if (!Objects.equals(oldVal, newVal)) {
                    changes.put(name, formatChange(oldVal, newVal));
                }
            } catch (IllegalAccessException e) { /* ignore */ }
        }
        return changes.toString();
    }

    // ==================== 私有方法 ====================

    private static String formatChange(Object oldVal, Object newVal) {
        String oldStr = oldVal == null ? "null" : oldVal.toString();
        String newStr = newVal == null ? "null" : newVal.toString();
        return oldStr + "→" + newStr;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    fields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    private static Set<String> toSet(String[] arr) {
        if (arr == null || arr.length == 0) return null;
        return new HashSet<>(Arrays.asList(arr));
    }
}