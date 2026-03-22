package org.example.utils;

import org.example.enums.OperationType;
import org.example.model.ChangeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AddLogHelper {
    private static final Logger logger = LoggerFactory.getLogger(AddLogHelper.class);

    public static <T> String getData(T entity) {
        if (entity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForAdd(entity);
            return ChangeResultFormatter.format(result, OperationType.ADD);
        } catch (Exception e) {
            logger.error("获取新增变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    public static <T> String getData(T entity, String... excludeFields) {
        return getData(entity, toSet(excludeFields));
    }

    public static <T> String getData(T entity, Set<String> excludeFields) {
        if (entity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForAdd(entity);
            return ChangeResultFormatter.formatWithExcludes(result, OperationType.ADD, excludeFields);
        } catch (Exception e) {
            logger.error("获取新增变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    public static <T> String getDataWithInclude(T entity, String... includeFields) {
        return getDataWithInclude(entity, toSet(includeFields));
    }

    public static <T> String getDataWithInclude(T entity, Set<String> includeFields) {
        if (entity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForAdd(entity);
            return ChangeResultFormatter.formatWithIncludes(result, OperationType.ADD, includeFields);
        } catch (Exception e) {
            logger.error("获取新增变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    private static Set<String> toSet(String[] arr) {
        if (arr == null || arr.length == 0) return null;
        return new HashSet<>(Arrays.asList(arr));
    }
}
