package org.example;

import org.example.enums.OperationType;
import org.example.model.AuditLog;
import org.example.model.ChangeResult;
import org.example.model.User;
import org.example.utils.ChangeResultFormatter;
import org.example.utils.EntityChangeProcessor;

public class Example1 {

    public static void main(String[] args) {

        // ========== 准备数据 ==========
        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setName("张三");
        oldUser.setAge(25);
        oldUser.setEmail(null);
        oldUser.setCreateTime(System.currentTimeMillis());

        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("张三");
        newUser.setAge(26);
        newUser.setEmail("zhangsan@example.com");
        newUser.setCreateTime(System.currentTimeMillis());

        // ========== 更新操作 ==========
        ChangeResult updateResult = EntityChangeProcessor.compareForUpdate(oldUser, newUser);

        // 格式化成 data 字符串，明确指定操作类型为 UPDATE
        String updateData = ChangeResultFormatter.format(updateResult, OperationType.UPDATE);

        // 组装完整的审计日志
        AuditLog updateLog = new AuditLog(
                10001L,
                "admin",
                "User",
                "1001",
                OperationType.UPDATE.getCode(),
                updateData
        );

        System.out.println("=== 更新操作 ===");
        System.out.println("变更内容: " + updateLog.getData());
        // 输出: age: 25→26, email: zhangsan@example.com

        // ========== 新增操作 ==========
        User newUser2 = new User();
        newUser2.setName("李四");
        newUser2.setAge(30);
        newUser2.setEmail("lisi@example.com");

        ChangeResult addResult = EntityChangeProcessor.compareForAdd(newUser2);
        String addData = ChangeResultFormatter.format(addResult, OperationType.ADD);

        AuditLog addLog = new AuditLog(
                10001L,
                "admin",
                "User",
                "1002",
                OperationType.ADD.getCode(),
                addData
        );

        System.out.println("\n=== 新增操作 ===");
        System.out.println("变更内容: " + addLog.getData());
        // 输出: name: 李四, age: 30, email: lisi@example.com

        // ========== 删除操作 ==========
        User deleteUser = new User();
        deleteUser.setName("王五");
        deleteUser.setAge(35);
        deleteUser.setEmail("wangwu@example.com");

        ChangeResult deleteResult = EntityChangeProcessor.compareForDelete(deleteUser);
        String deleteData = ChangeResultFormatter.format(deleteResult, OperationType.DELETE);

        AuditLog deleteLog = new AuditLog(
                10001L,
                "admin",
                "User",
                "1003",
                OperationType.DELETE.getCode(),
                deleteData
        );

        System.out.println("\n=== 删除操作 ===");
        System.out.println("变更内容: " + deleteLog.getData());
        // 输出: name: 王五, age: 35, email: wangwu@example.com

        // ========== 自定义排除字段 ==========
        String customData = ChangeResultFormatter.format(updateResult, OperationType.UPDATE, "email");
        System.out.println("\n=== 排除email字段 ===");
        System.out.println(customData);
        // 输出: age: 25→26

        // ========== 保存到数据库 ==========
        // auditLogMapper.insert(updateLog);
        // auditLogMapper.insert(addLog);
        // auditLogMapper.insert(deleteLog);
    }
}
