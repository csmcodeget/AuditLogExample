package org.example.service;

import org.example.enums.OperationType;
import org.example.model.AuditLog;
import org.example.model.ChangeResult;
import org.example.utils.ChangeResultFormatter;
import org.example.utils.EntityChangeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 审计日志服务 - 同步版本（无线程池）
 *
 * 功能说明：
 * 1. 支持新增、删除、更新三种操作
 * 2. 支持 excludeFields（排除字段）
 * 3. 支持 includeFields（关注字段，只记录指定字段）
 * 4. 两者可同时使用，优先级：includeFields > excludeFields
 * 5. 默认排除系统字段（id、createTime等）
 * 6. 同步保存，日志记录失败不影响主业务（异常被捕获）
 */
public class AuditService2 {

    private static final Logger logger = LoggerFactory.getLogger(AuditService2.class);

    /**
     * 日志存储回调接口（由调用方实现）
     */
    private static AuditLogSaver logSaver;

    /**
     * 设置日志存储回调
     */
    public static void setLogSaver(AuditLogSaver saver) {
        AuditService2.logSaver = saver;
    }

    // ==================== 新增操作 ====================

    /**
     * 记录新增操作（使用默认排除字段）
     */
    public static <T> void logAdd(T entity, Object entityId, Long userId,
                                  String userName, String businessType) {
        logAdd(entity, entityId, userId, userName, businessType, (Set<String>) null, null);
    }

    /**
     * 记录新增操作（排除指定字段）
     */
    public static <T> void logAdd(T entity, Object entityId, Long userId,
                                  String userName, String businessType,
                                  String... excludeFields) {
        Set<String> excludeSet = excludeFields != null && excludeFields.length > 0
                ? new HashSet<>(Arrays.asList(excludeFields))
                : null;
        logAdd(entity, entityId, userId, userName, businessType, excludeSet, null);
    }

    /**
     * 记录新增操作（只关注指定字段）
     */
    public static <T> void logAddWithInclude(T entity, Object entityId, Long userId,
                                             String userName, String businessType,
                                             String... includeFields) {
        Set<String> includeSet = includeFields != null && includeFields.length > 0
                ? new HashSet<>(Arrays.asList(includeFields))
                : null;
        logAdd(entity, entityId, userId, userName, businessType, null, includeSet);
    }

    /**
     * 记录新增操作（完整参数）
     *
     * @param entity 新增的实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     * @param excludeFields 排除的字段（可选）
     * @param includeFields 关注的字段（可选，如果指定则只记录这些字段）
     */
    public static <T> void logAdd(T entity, Object entityId, Long userId,
                                  String userName, String businessType,
                                  Set<String> excludeFields, Set<String> includeFields) {
        if (entity == null) {
            logger.warn("新增日志记录失败: entity为null");
            return;
        }

        try {
            ChangeResult result = EntityChangeProcessor.compareForAdd(entity);
            String data = ChangeResultFormatter.format(result, OperationType.ADD,
                    excludeFields, includeFields);

            AuditLog auditLog = new AuditLog(
                    userId,
                    userName,
                    businessType,
                    entityId != null ? entityId.toString() : "",
                    OperationType.ADD.getCode(),
                    data
            );

            saveLog(auditLog);
        } catch (Exception e) {
            logger.error("记录新增日志失败: {}", e.getMessage(), e);
        }
    }

    // ==================== 删除操作 ====================

    /**
     * 记录删除操作（使用默认排除字段）
     */
    public static <T> void logDelete(T entity, Object entityId, Long userId,
                                     String userName, String businessType) {
        logDelete(entity, entityId, userId, userName, businessType, (Set<String>) null, null);
    }

    /**
     * 记录删除操作（排除指定字段）
     */
    public static <T> void logDelete(T entity, Object entityId, Long userId,
                                     String userName, String businessType,
                                     String... excludeFields) {
        Set<String> excludeSet = excludeFields != null && excludeFields.length > 0
                ? new HashSet<>(Arrays.asList(excludeFields))
                : null;
        logDelete(entity, entityId, userId, userName, businessType, excludeSet, null);
    }

    /**
     * 记录删除操作（只关注指定字段）
     */
    public static <T> void logDeleteWithInclude(T entity, Object entityId, Long userId,
                                                String userName, String businessType,
                                                String... includeFields) {
        Set<String> includeSet = includeFields != null && includeFields.length > 0
                ? new HashSet<>(Arrays.asList(includeFields))
                : null;
        logDelete(entity, entityId, userId, userName, businessType, null, includeSet);
    }

    /**
     * 记录删除操作（完整参数）
     */
    public static <T> void logDelete(T entity, Object entityId, Long userId,
                                     String userName, String businessType,
                                     Set<String> excludeFields, Set<String> includeFields) {
        if (entity == null) {
            logger.warn("删除日志记录失败: entity为null");
            return;
        }

        try {
            ChangeResult result = EntityChangeProcessor.compareForDelete(entity);
            String data = ChangeResultFormatter.format(result, OperationType.DELETE,
                    excludeFields, includeFields);

            AuditLog auditLog = new AuditLog(
                    userId,
                    userName,
                    businessType,
                    entityId != null ? entityId.toString() : "",
                    OperationType.DELETE.getCode(),
                    data
            );

            saveLog(auditLog);
        } catch (Exception e) {
            logger.error("记录删除日志失败: {}", e.getMessage(), e);
        }
    }

    // ==================== 更新操作 ====================

    /**
     * 记录更新操作（使用默认排除字段）
     */
    public static <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                                     Long userId, String userName, String businessType) {
        logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType,
                (Set<String>) null, null);
    }

    /**
     * 记录更新操作（排除指定字段）
     */
    public static <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                                     Long userId, String userName, String businessType,
                                     String... excludeFields) {
        Set<String> excludeSet = excludeFields != null && excludeFields.length > 0
                ? new HashSet<>(Arrays.asList(excludeFields))
                : null;
        logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType,
                excludeSet, null);
    }

    /**
     * 记录更新操作（只关注指定字段）
     */
    public static <T> void logUpdateWithInclude(T oldEntity, T newEntity, Object entityId,
                                                Long userId, String userName, String businessType,
                                                String... includeFields) {
        Set<String> includeSet = includeFields != null && includeFields.length > 0
                ? new HashSet<>(Arrays.asList(includeFields))
                : null;
        logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType,
                null, includeSet);
    }

    /**
     * 记录更新操作（完整参数）
     *
     * @param oldEntity 旧实体
     * @param newEntity 新实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     * @param excludeFields 排除的字段（可选）
     * @param includeFields 关注的字段（可选，如果指定则只记录这些字段）
     */
    public static <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                                     Long userId, String userName, String businessType,
                                     Set<String> excludeFields, Set<String> includeFields) {
        if (oldEntity == null || newEntity == null) {
            logger.warn("更新日志记录失败: oldEntity或newEntity为null");
            return;
        }

        try {
            ChangeResult result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
            String data = ChangeResultFormatter.format(result, OperationType.UPDATE, excludeFields, includeFields);

            AuditLog auditLog = new AuditLog(
                    userId,
                    userName,
                    businessType,
                    entityId != null ? entityId.toString() : "",
                    OperationType.UPDATE.getCode(),
                    data
            );

            saveLog(auditLog);
        } catch (Exception e) {
            logger.error("记录更新日志失败: {}", e.getMessage(), e);
        }
    }

    // ==================== 通用方法（自动判断操作类型） ====================

    /**
     * 通用记录方法（自动判断操作类型）
     */
    public static <T> void log(T oldEntity, T newEntity, Object entityId,
                               Long userId, String userName, String businessType) {
        log(oldEntity, newEntity, entityId, userId, userName, businessType,
                (Set<String>) null, null);
    }

    /**
     * 通用记录方法（自动判断操作类型，排除指定字段）
     */
    public static <T> void log(T oldEntity, T newEntity, Object entityId,
                               Long userId, String userName, String businessType,
                               String... excludeFields) {
        Set<String> excludeSet = excludeFields != null && excludeFields.length > 0
                ? new HashSet<>(Arrays.asList(excludeFields))
                : null;
        log(oldEntity, newEntity, entityId, userId, userName, businessType,
                excludeSet, null);
    }

    /**
     * 通用记录方法（自动判断操作类型，只关注指定字段）
     */
    public static <T> void logWithInclude(T oldEntity, T newEntity, Object entityId,
                                          Long userId, String userName, String businessType,
                                          String... includeFields) {
        Set<String> includeSet = includeFields != null && includeFields.length > 0
                ? new HashSet<>(Arrays.asList(includeFields))
                : null;
        log(oldEntity, newEntity, entityId, userId, userName, businessType,
                null, includeSet);
    }

    /**
     * 通用记录方法（完整参数，自动判断操作类型）
     */
    public static <T> void log(T oldEntity, T newEntity, Object entityId,
                               Long userId, String userName, String businessType,
                               Set<String> excludeFields, Set<String> includeFields) {
        if (oldEntity == null && newEntity != null) {
            // 新增
            logAdd(newEntity, entityId, userId, userName, businessType, excludeFields, includeFields);
        } else if (oldEntity != null && newEntity == null) {
            // 删除
            logDelete(oldEntity, entityId, userId, userName, businessType, excludeFields, includeFields);
        } else if (oldEntity != null && newEntity != null) {
            // 更新
            logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType, excludeFields, includeFields);
        } else {
            logger.warn("oldEntity和newEntity都为null，无法记录日志");
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 获取变更数据字符串（不保存）
     */
    public static <T> String getChangeData(T oldEntity, T newEntity, OperationType operationType) {
        return getChangeData(oldEntity, newEntity, operationType, (Set<String>) null, null);
    }

    /**
     * 获取变更数据字符串（排除指定字段）
     */
    public static <T> String getChangeData(T oldEntity, T newEntity,
                                           OperationType operationType,
                                           String... excludeFields) {
        Set<String> excludeSet = excludeFields != null && excludeFields.length > 0
                ? new HashSet<>(Arrays.asList(excludeFields))
                : null;
        return getChangeData(oldEntity, newEntity, operationType, excludeSet, null);
    }

    /**
     * 获取变更数据字符串（只关注指定字段）
     */
    public static <T> String getChangeDataWithInclude(T oldEntity, T newEntity,
                                                      OperationType operationType,
                                                      String... includeFields) {
        Set<String> includeSet = includeFields != null && includeFields.length > 0
                ? new HashSet<>(Arrays.asList(includeFields))
                : null;
        return getChangeData(oldEntity, newEntity, operationType, null, includeSet);
    }

    /**
     * 获取变更数据字符串（完整参数）
     */
    public static <T> String getChangeData(T oldEntity, T newEntity,
                                           OperationType operationType,
                                           Set<String> excludeFields, Set<String> includeFields) {
        try {
            ChangeResult result;
            switch (operationType) {
                case ADD:
                    result = EntityChangeProcessor.compareForAdd(newEntity);
                    break;
                case DELETE:
                    result = EntityChangeProcessor.compareForDelete(oldEntity);
                    break;
                case UPDATE:
                    result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
                    break;
                default:
                    return "";
            }
            return ChangeResultFormatter.format(result, operationType, excludeFields, includeFields);
        } catch (Exception e) {
            logger.error("获取变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    // ==================== 保存日志 ====================

    /**
     * 保存日志（同步）
     */
    private static void saveLog(AuditLog auditLog) {
        if (logSaver != null) {
            try {
                logSaver.save(auditLog);
                logger.debug("审计日志保存成功: {}", auditLog.getBusinessType() + ":" + auditLog.getBusinessId());
            } catch (Exception e) {
                logger.error("保存审计日志失败: {}", e.getMessage(), e);
            }
        } else {
            // 没有设置存储回调，只打印日志
            logger.info("审计日志: {}", auditLog);
        }
    }

    /**
     * 日志存储回调接口
     */
    public interface AuditLogSaver {
        void save(AuditLog auditLog);
    }
}