package org.example.service;

// AuditService.java - 保持静态工具类
import org.example.enums.OperationType;
import org.example.model.AuditLog;
import org.example.utils.AddLogHelper;
import org.example.utils.DeleteLogHelper;
import org.example.utils.UpdateLogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 审计日志工具类（静态方法）
 */
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    private static AuditLogSaver logSaver;

    /**
     * 设置日志保存器（全局配置一次）
     */
    public static void setLogSaver(AuditLogSaver saver) {
        logSaver = saver;
    }

    // ========== 新增日志 ==========

    public static <T> void saveAddLog(T entity, Long userId, String userName,
                                      String businessType, String businessId) {
        if (entity == null) {
            logger.warn("保存新增日志失败: entity为null");
            return;
        }
        String data = AddLogHelper.getData(entity);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.ADD);
    }

    public static <T> void saveAddLogWithIncludes(T entity, Long userId, String userName,
                                                  String businessType, String businessId,
                                                  String... includeFields) {
        if (entity == null) {
            logger.warn("保存新增日志失败: entity为null");
            return;
        }
        String data = AddLogHelper.getDataWithInclude(entity, includeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.ADD);
    }

    public static <T> void saveAddLogWithExcludes(T entity, Long userId, String userName,
                                                  String businessType, String businessId,
                                                  String... excludeFields) {
        if (entity == null) {
            logger.warn("保存新增日志失败: entity为null");
            return;
        }
        String data = AddLogHelper.getData(entity, excludeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.ADD);
    }

    // ========== 删除日志 ==========

    public static <T> void saveDeleteLog(T entity, Long userId, String userName,
                                         String businessType, String businessId) {
        if (entity == null) {
            logger.warn("保存删除日志失败: entity为null");
            return;
        }
        String data = DeleteLogHelper.getData(entity);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.DELETE);
    }

    public static <T> void saveDeleteLogWithIncludes(T entity, Long userId, String userName,
                                                     String businessType, String businessId,
                                                     String... includeFields) {
        if (entity == null) {
            logger.warn("保存删除日志失败: entity为null");
            return;
        }
        String data = DeleteLogHelper.getDataWithInclude(entity, includeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.DELETE);
    }

    public static <T> void saveDeleteLogWithExcludes(T entity, Long userId, String userName,
                                                     String businessType, String businessId,
                                                     String... excludeFields) {
        if (entity == null) {
            logger.warn("保存删除日志失败: entity为null");
            return;
        }
        String data = DeleteLogHelper.getData(entity, excludeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.DELETE);
    }

    // ========== 更新日志 ==========

    public static <T> void saveUpdateLog(T oldEntity, T newEntity, Long userId, String userName,
                                         String businessType, String businessId) {
        if (oldEntity == null || newEntity == null) {
            logger.warn("保存更新日志失败: oldEntity或newEntity为null");
            return;
        }
        String data = UpdateLogHelper.getData(oldEntity, newEntity);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.UPDATE);
    }

    public static <T> void saveUpdateLogWithIncludes(T oldEntity, T newEntity, Long userId, String userName,
                                                     String businessType, String businessId,
                                                     String... includeFields) {
        if (oldEntity == null || newEntity == null) {
            logger.warn("保存更新日志失败: oldEntity或newEntity为null");
            return;
        }
        String data = UpdateLogHelper.getDataWithInclude(oldEntity, newEntity, includeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.UPDATE);
    }

    public static <T> void saveUpdateLogWithExcludes(T oldEntity, T newEntity, Long userId, String userName,
                                                     String businessType, String businessId,
                                                     String... excludeFields) {
        if (oldEntity == null || newEntity == null) {
            logger.warn("保存更新日志失败: oldEntity或newEntity为null");
            return;
        }
        String data = UpdateLogHelper.getData(oldEntity, newEntity, excludeFields);
        saveIfNotEmpty(data, userId, userName, businessType, businessId, OperationType.UPDATE);
    }

    // ========== 私有方法 ==========

    private static void saveIfNotEmpty(String data, Long userId, String userName,
                                       String businessType, String businessId,
                                       OperationType operationType) {
        if (data == null || data.isEmpty()) {
            return;
        }
        save(new AuditLog(userId, userName, businessType, businessId,
                operationType.getCode(), data));
    }

    private static void save(AuditLog log) {
        if (logSaver != null) {
            try {
                logSaver.save(log);
            } catch (Exception e) {
                logger.error("保存审计日志失败: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("未设置日志保存器，审计日志未保存: {}", log);
        }
    }

    public interface AuditLogSaver {
        void save(AuditLog auditLog);
    }
}