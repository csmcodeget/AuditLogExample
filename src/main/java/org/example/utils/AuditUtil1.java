package org.example.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 审计日志工具 - 最终版
 * 就这样了，不再改了
 */
public class AuditUtil1 {

    // ==================== 新增 ====================
    public static String getAddData(Object entity) {
        return getAddData(entity, (Set<String>) null);
    }

    public static String getAddData(Object entity, String... includeFields) {
        return getAddData(entity, toSet(includeFields));
    }

    public static String getAddData(Object entity, Set<String> includeFields) {
        if (entity == null) return "";
        Map<String, Object> data = new LinkedHashMap<>();
        for (Field field : getAllFields(entity.getClass())) {
            String name = field.getName();
            if (includeFields != null && !includeFields.isEmpty() && !includeFields.contains(name)) continue;
            field.setAccessible(true);
            try {
                Object val = field.get(entity);
                if (val != null) data.put(name, val);
            } catch (IllegalAccessException e) { /* ignore */ }
        }
        return data.toString();
    }

    // ==================== 删除 ====================
    public static String getDeleteData(Object entity) {
        return getAddData(entity);
    }

    public static String getDeleteData(Object entity, String... includeFields) {
        return getAddData(entity, includeFields);
    }

    public static String getDeleteData(Object entity, Set<String> includeFields) {
        return getAddData(entity, includeFields);
    }

    // ==================== 更新 ====================
    public static String getUpdateData(Object oldObj, Object newObj) {
        return getUpdateData(oldObj, newObj, (Set<String>) null);
    }

    public static String getUpdateData(Object oldObj, Object newObj, String... includeFields) {
        return getUpdateData(oldObj, newObj, toSet(includeFields));
    }

    public static String getUpdateData(Object oldObj, Object newObj, Set<String> includeFields) {
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
                    changes.put(name, (oldVal == null ? "null" : oldVal) + "→" + (newVal == null ? "null" : newVal));
                }
            } catch (IllegalAccessException e) { /* ignore */ }
        }
        return changes.toString();
    }

    // ==================== 通用 ====================
    public static String getChangeData(Object oldObj, Object newObj) {
        return getChangeData(oldObj, newObj, (Set<String>) null);
    }

    public static String getChangeData(Object oldObj, Object newObj, String... includeFields) {
        return getChangeData(oldObj, newObj, toSet(includeFields));
    }

    public static String getChangeData(Object oldObj, Object newObj, Set<String> includeFields) {
        if (oldObj == null && newObj != null) return getAddData(newObj, includeFields);
        if (oldObj != null && newObj == null) return getDeleteData(oldObj, includeFields);
        if (oldObj != null && newObj != null) return getUpdateData(oldObj, newObj, includeFields);
        return "";
    }

    // ==================== 私有 ====================
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field f : clazz.getDeclaredFields()) {
                int mod = f.getModifiers();
                if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static Set<String> toSet(String[] arr) {
        return (arr == null || arr.length == 0) ? null : new HashSet<>(Arrays.asList(arr));
    }
//    // 就这样用，别想太多
//    String log = AuditUtil.getAddData(user);
//    String log = AuditUtil.getUpdateData(oldUser, newUser, "name", "age");
//    String log = AuditUtil.getChangeData(oldUser, newUser, Set.of("name", "age"));
}