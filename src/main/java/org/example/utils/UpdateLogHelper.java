package org.example.utils;

import org.example.enums.OperationType;
import org.example.model.ChangeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UpdateLogHelper {
    private static final Logger logger = LoggerFactory.getLogger(UpdateLogHelper.class);

    public static <T> String getData(T oldEntity, T newEntity) {
        if (oldEntity == null || newEntity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
            return ChangeResultFormatter.format(result, OperationType.UPDATE);
        } catch (Exception e) {
            logger.error("获取更新变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    public static <T> String getData(T oldEntity, T newEntity, String... excludeFields) {
        return getData(oldEntity, newEntity, toSet(excludeFields));
    }

    public static <T> String getData(T oldEntity, T newEntity, Set<String> excludeFields) {
        if (oldEntity == null || newEntity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
            return ChangeResultFormatter.formatWithExcludes(result, OperationType.UPDATE, excludeFields);
        } catch (Exception e) {
            logger.error("获取更新变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    public static <T> String getDataWithInclude(T oldEntity, T newEntity, String... includeFields) {
        return getDataWithInclude(oldEntity, newEntity, toSet(includeFields));
    }

    public static <T> String getDataWithInclude(T oldEntity, T newEntity, Set<String> includeFields) {
        if (oldEntity == null || newEntity == null) return "";
        try {
            ChangeResult result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
            return ChangeResultFormatter.formatWithIncludes(result, OperationType.UPDATE, includeFields);
        } catch (Exception e) {
            logger.error("获取更新变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    private static Set<String> toSet(String[] arr) {
        if (arr == null || arr.length == 0) return null;
        return new HashSet<>(Arrays.asList(arr));
    }
}
