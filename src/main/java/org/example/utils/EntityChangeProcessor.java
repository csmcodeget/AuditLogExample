package org.example.utils;

import org.example.model.ChangeResult;
import org.example.model.FieldChange;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 实体变更处理器 - 只负责对比，返回字段变化
 */
public class EntityChangeProcessor {

    private static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static <T> ChangeResult compareForAdd(T entity) {
        ChangeResult result = new ChangeResult();
        if (entity == null) {
            result.addFailedField("entity", new NullPointerException("entity is null"));
            return result;
        }

        for (Field field : getDeclaredFields(entity.getClass())) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    result.addChange(new FieldChange(field.getName(), null, value, field.getType()));
                }
            } catch (IllegalAccessException e) {
                result.addFailedField(field.getName(), e);
            }
        }
        return result;
    }

    public static <T> ChangeResult compareForDelete(T entity) {
        ChangeResult result = new ChangeResult();
        if (entity == null) {
            result.addFailedField("entity", new NullPointerException("entity is null"));
            return result;
        }

        for (Field field : getDeclaredFields(entity.getClass())) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    result.addChange(new FieldChange(field.getName(), value, null, field.getType()));
                }
            } catch (IllegalAccessException e) {
                result.addFailedField(field.getName(), e);
            }
        }
        return result;
    }

    public static <T> ChangeResult compareForUpdate(T oldEntity, T newEntity) {
        ChangeResult result = new ChangeResult();
        if (oldEntity == null || newEntity == null) {
            result.addFailedField("entity", new NullPointerException("oldEntity or newEntity is null"));
            return result;
        }

        if (!oldEntity.getClass().equals(newEntity.getClass())) {
            result.addFailedField("entityType", new IllegalArgumentException("entity types not match"));
            return result;
        }

        for (Field field : getDeclaredFields(oldEntity.getClass())) {
            field.setAccessible(true);
            try {
                Object oldValue = field.get(oldEntity);
                Object newValue = field.get(newEntity);
                if (!Objects.equals(oldValue, newValue)) {
                    result.addChange(new FieldChange(field.getName(), oldValue, newValue, field.getType()));
                }
            } catch (IllegalAccessException e) {
                result.addFailedField(field.getName(), e);
            }
        }
        return result;
    }
}