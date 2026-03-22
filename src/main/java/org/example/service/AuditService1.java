package org.example.service;

import org.example.enums.OperationType;
import org.example.model.AuditLog;
import org.example.model.ChangeResult;
import org.example.utils.ChangeResultFormatter;
import org.example.utils.EntityChangeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 审计日志服务 - 统一入口
 */

public class AuditService1 {

    private static final Logger logger = LoggerFactory.getLogger(AuditService1.class);

//    @Autowired(required = false)
//    private AuditLogMapper auditLogMapper;

    /**
     * 记录新增操作
     *
     * @param entity 新增的实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     */
    public <T> void logAdd(T entity, Object entityId, Long userId,
                           String userName, String businessType) {
        logAdd(entity, entityId, userId, userName, businessType, (Set<String>) null);
    }

    /**
     * 记录新增操作（自定义排除字段）
     */
    public <T> void logAdd(T entity, Object entityId, Long userId,
                           String userName, String businessType,
                           Set<String> customExcludeFields) {
        try {
            ChangeResult result = EntityChangeProcessor.compareForAdd(entity);
            String data = ChangeResultFormatter.format(result, OperationType.ADD, customExcludeFields);

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

    /**
     * 记录新增操作（自定义排除字段列表）
     */
    public <T> void logAdd(T entity, Object entityId, Long userId,
                           String userName, String businessType,
                           String... customExcludeFields) {
        Set<String> excludeSet = customExcludeFields != null
                ? Set.of(customExcludeFields)
                : null;
        logAdd(entity, entityId, userId, userName, businessType, excludeSet);
    }

    /**
     * 记录删除操作
     *
     * @param entity 删除的实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     */
    public <T> void logDelete(T entity, Object entityId, Long userId,
                              String userName, String businessType) {
        logDelete(entity, entityId, userId, userName, businessType, (Set<String>) null);
    }

    /**
     * 记录删除操作（自定义排除字段）
     */
    public <T> void logDelete(T entity, Object entityId, Long userId,
                              String userName, String businessType,
                              Set<String> customExcludeFields) {
        try {
            ChangeResult result = EntityChangeProcessor.compareForDelete(entity);
            String data = ChangeResultFormatter.format(result, OperationType.DELETE, customExcludeFields);

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

    /**
     * 记录删除操作（自定义排除字段列表）
     */
    public <T> void logDelete(T entity, Object entityId, Long userId,
                              String userName, String businessType,
                              String... customExcludeFields) {
        Set<String> excludeSet = customExcludeFields != null
                ? Set.of(customExcludeFields)
                : null;
        logDelete(entity, entityId, userId, userName, businessType, excludeSet);
    }

    /**
     * 记录更新操作
     *
     * @param oldEntity 旧实体
     * @param newEntity 新实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     */
    public <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                              Long userId, String userName, String businessType) {
        logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType, (Set<String>) null);
    }

    /**
     * 记录更新操作（自定义排除字段）
     */
    public <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                              Long userId, String userName, String businessType,
                              Set<String> customExcludeFields) {
        try {
            ChangeResult result = EntityChangeProcessor.compareForUpdate(oldEntity, newEntity);
            String data = ChangeResultFormatter.format(result, OperationType.UPDATE, customExcludeFields);

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

    /**
     * 记录更新操作（自定义排除字段列表）
     */
    public <T> void logUpdate(T oldEntity, T newEntity, Object entityId,
                              Long userId, String userName, String businessType,
                              String... customExcludeFields) {
        Set<String> excludeSet = customExcludeFields != null
                ? Set.of(customExcludeFields)
                : null;
        logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType, excludeSet);
    }

    /**
     * 通用记录方法（根据是否有旧实体自动判断操作类型）
     *
     * @param oldEntity 旧实体（新增时为null）
     * @param newEntity 新实体
     * @param entityId 实体ID
     * @param userId 操作人ID
     * @param userName 操作人姓名
     * @param businessType 业务类型
     */
    public <T> void log(T oldEntity, T newEntity, Object entityId,
                        Long userId, String userName, String businessType) {
        log(oldEntity, newEntity, entityId, userId, userName, businessType, (Set<String>) null);
    }

    /**
     * 通用记录方法（自定义排除字段）
     */
    public <T> void log(T oldEntity, T newEntity, Object entityId,
                        Long userId, String userName, String businessType,
                        Set<String> customExcludeFields) {
        if (oldEntity == null && newEntity != null) {
            // 新增
            logAdd(newEntity, entityId, userId, userName, businessType, customExcludeFields);
        } else if (oldEntity != null && newEntity == null) {
            // 删除
            logDelete(oldEntity, entityId, userId, userName, businessType, customExcludeFields);
        } else if (oldEntity != null && newEntity != null) {
            // 更新
            logUpdate(oldEntity, newEntity, entityId, userId, userName, businessType, customExcludeFields);
        } else {
            logger.warn("oldEntity和newEntity都为null，无法记录日志");
        }
    }

    /**
     * 通用记录方法（自定义排除字段列表）
     */
    public <T> void log(T oldEntity, T newEntity, Object entityId,
                        Long userId, String userName, String businessType,
                        String... customExcludeFields) {
        Set<String> excludeSet = customExcludeFields != null
                ? Set.of(customExcludeFields)
                : null;
        log(oldEntity, newEntity, entityId, userId, userName, businessType, excludeSet);
    }

    /**
     * 获取变更数据字符串（不保存到数据库）
     * 用于需要预览或发送通知的场景
     */
    public <T> String getChangeData(T oldEntity, T newEntity, OperationType operationType) {
        return getChangeData(oldEntity, newEntity, operationType, (Set<String>) null);
    }

    /**
     * 获取变更数据字符串（自定义排除字段）
     */
    public <T> String getChangeData(T oldEntity, T newEntity,
                                    OperationType operationType,
                                    Set<String> customExcludeFields) {
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
            return ChangeResultFormatter.format(result, operationType, customExcludeFields);
        } catch (Exception e) {
            logger.error("获取变更数据失败: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 保存日志（支持异步）
     */
    private void saveLog(AuditLog auditLog) {
//        // 如果有 Mapper，保存到数据库
//        if (auditLogMapper != null) {
//            // 方式1：同步保存
//            // auditLogMapper.insert(auditLog);
//
//            // 方式2：异步保存（推荐）
//            CompletableFuture.runAsync(() -> {
//                try {
//                    auditLogMapper.insert(auditLog);
//                } catch (Exception e) {
//                    logger.error("保存审计日志失败: {}", e.getMessage(), e);
//                }
//            });
//        } else {
//            // 没有 Mapper，只打印日志
//            logger.info("审计日志: {}", auditLog);
//        }
    }
}