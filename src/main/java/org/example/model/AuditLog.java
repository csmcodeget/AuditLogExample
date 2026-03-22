package org.example.model;
import java.time.LocalDateTime;

/**
 * 审计日志实体
 */
public class AuditLog {

    private Long id;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    private Long userId;
    private String userName;
    private String businessType;
    private String businessId;
    private String action;
    private String data;
    private LocalDateTime operationTime;

    public AuditLog() {
        this.operationTime = LocalDateTime.now();
    }

    public AuditLog(Long userId, String userName, String businessType,
                    String businessId, String action, String data) {
        this();
        this.userId = userId;
        this.userName = userName;
        this.businessType = businessType;
        this.businessId = businessId;
        this.action = action;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("AuditLog{userId=%d, userName='%s', businessType='%s', businessId='%s', action='%s', data='%s'}",
                userId, userName, businessType, businessId, action, data);
    }

    // getter/setter 省略...
}