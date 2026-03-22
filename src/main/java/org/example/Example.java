package org.example;

import org.example.service.AuditService3;

public class Example {
    // 示例实体类
    public static class User {
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private String password;

        public User() {}

        public User(Long id, String name, Integer age, String email, String password) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.email = email;
            this.password = password;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static void main(String[] args) {

        // 设置日志保存器（实际项目中可注入数据库Mapper）
        AuditService3.setLogSaver(log -> {
            System.out.println("保存审计日志: " + log);
            // 实际使用时: auditLogMapper.insert(log);
        });

        Long userId = 100L;
        String userName = "操作员";
        String businessType = "USER";
        String businessId = "1";

        // 准备测试数据
        User newUser = new User(1L, "张三", 25, "zhangsan@example.com", "123456");
        User oldUser = new User(1L, "张三", 24, "zhangsan@example.com", "123456");
        User updatedUser = new User(1L, "张三", 26, "zhangsan_new@example.com", "123456");

        // ========== 新增日志 ==========
        System.out.println("\n=== 新增日志 ===");
        AuditService3.saveAddLog(newUser, userId, userName, businessType, businessId);
        AuditService3.saveAddLogWithIncludes(newUser, userId, userName, businessType, businessId, "name", "age");
        AuditService3.saveAddLogWithExcludes(newUser, userId, userName, businessType, businessId, "password", "email");

        // ========== 删除日志 ==========
        System.out.println("\n=== 删除日志 ===");
        AuditService3.saveDeleteLog(newUser, userId, userName, businessType, businessId);
        AuditService3.saveDeleteLogWithIncludes(newUser, userId, userName, businessType, businessId, "name", "id");
        AuditService3.saveDeleteLogWithExcludes(newUser, userId, userName, businessType, businessId, "password");

        // ========== 更新日志 ==========
        System.out.println("\n=== 更新日志 ===");
        AuditService3.saveUpdateLog(oldUser, updatedUser, userId, userName, businessType, businessId);
        AuditService3.saveUpdateLogWithIncludes(oldUser, updatedUser, userId, userName, businessType, businessId, "age", "email");
        AuditService3.saveUpdateLogWithExcludes(oldUser, updatedUser, userId, userName, businessType, businessId, "password");
    }
}
