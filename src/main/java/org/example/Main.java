package org.example;

import org.example.enums.OperationType;
import org.example.model.User;
import org.example.service.AuditService2;

// 按两次 Shift 打开“随处搜索”对话框并输入 `show whitespaces`，
// 然后按 Enter 键。现在，您可以在代码中看到空格字符。
public class Main {
    public static void main(String[] args) {
//        // 当文本光标位于高亮显示的文本处时按 Alt+Enter，
//        // 可查看 IntelliJ IDEA 对于如何修正该问题的建议。
//        System.out.printf("Hello and welcome!");
//
//        // 按 Shift+F10 或点击间距中的绿色箭头按钮以运行脚本。
//        for (int i = 1; i <= 5; i++) {
//
//            // 按 Shift+F9 开始调试代码。我们已为您设置了一个断点，
//            // 但您始终可以通过按 Ctrl+F8 添加更多断点。
//            System.out.println("i = " + i);
//        }


        // 可选：设置日志存储方式（比如保存到文件或数据库）
        // AuditService.setLogSaver(auditLog -> {
        //     // 自定义存储逻辑
        //     System.out.println("保存到数据库: " + auditLog);
        // });

        // 可选：设置同步/异步模式（默认异步）
        // AuditService.setAsyncMode(false);

        System.out.println("========== 审计日志示例 ==========\n");

        // ========== 1. 新增操作 ==========
        User newUser = new User();
        newUser.setName("张三");
        newUser.setAge(25);
        newUser.setEmail("zhangsan@example.com");

        AuditService2.logAdd(newUser, 1001L, 10001L, "admin", "User");
        System.out.println("✓ 新增日志已记录");

        // ========== 2. 更新操作 ==========
        User oldUser = new User();
        oldUser.setName("张三");
        oldUser.setAge(25);
        oldUser.setEmail("zhangsan@example.com");

        User updatedUser = new User();
        updatedUser.setName("张三");
        updatedUser.setAge(26);
        updatedUser.setEmail("zhangsan_new@example.com");

        AuditService2.logUpdate(oldUser, updatedUser, 1001L, 10001L, "admin", "User");
        System.out.println("✓ 更新日志已记录");

        // ========== 3. 删除操作 ==========
        User deleteUser = new User();
        deleteUser.setName("李四");
        deleteUser.setAge(30);
        deleteUser.setEmail("lisi@example.com");

        AuditService2.logDelete(deleteUser, 1002L, 10001L, "admin", "User");
        System.out.println("✓ 删除日志已记录");

        // ========== 4. 自定义排除字段 ==========
        AuditService2.logUpdate(oldUser, updatedUser, 1001L, 10001L, "admin", "User", "email");
        System.out.println("✓ 排除email字段的更新日志已记录");

        // ========== 5. 通用方法（自动判断操作类型） ==========
        // 新增
        AuditService2.log(null, newUser, 1003L, 10001L, "admin", "User");
        // 删除
        AuditService2.log(deleteUser, null, 1004L, 10001L, "admin", "User");
        // 更新
        AuditService2.log(oldUser, updatedUser, 1005L, 10001L, "admin", "User");
        System.out.println("✓ 通用方法日志已记录");

        // ========== 6. 获取变更数据（不保存） ==========
        String changeData = AuditService2.getChangeData(oldUser, updatedUser, OperationType.UPDATE);
        System.out.println("\n变更数据预览: " + changeData);

        // 等待异步任务完成（生产环境一般不需要，这里演示用）
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 关闭线程池
        AuditService2.shutdown();

        System.out.println("\n========== 示例结束 ==========");
    }
}