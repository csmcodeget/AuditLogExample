import org.example.utils.SimpleAuditUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SimpleExample {
    // 父类
    public static class BaseEntity {
        private Long id;
        private Date createTime;
        private Date updateTime;

        public BaseEntity(Long id) {
            this.id = id;
            this.createTime = new Date();
            this.updateTime = new Date();
        }

        public Long getId() { return id; }
        public Date getCreateTime() { return createTime; }
        public Date getUpdateTime() { return updateTime; }
    }

    // 子类
    public static class User extends BaseEntity {
        private String name;
        private Integer age;
        private String email;
        private String password;

        public User(Long id, String name, Integer age, String email, String password) {
            super(id);
            this.name = name;
            this.age = age;
            this.email = email;
            this.password = password;
        }

        public String getName() { return name; }
        public Integer getAge() { return age; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static void main(String[] args) {

        User newUser = new User(1L, "张三", 25, "zhangsan@example.com", "123456");
        User oldUser = new User(1L, "张三", 24, "zhangsan@example.com", "123456");
        User updatedUser = new User(1L, "张三", 26, "zhangsan_new@example.com", "123456");

        // ========== 测试父类字段 ==========
        System.out.println("=== 测试父类字段 ===");
        System.out.println("新增(所有字段，包含父类): " + SimpleAuditUtil.getAddData(newUser));
        // 输出包含: id, createTime, updateTime, name, age, email, password

        // ========== 新增日志 - 使用 Set ==========
        System.out.println("\n=== 新增日志 ===");

        // 方式1：可变参数
        System.out.println("新增(可变参数): " + SimpleAuditUtil.getAddData(newUser, "name", "age", "email"));

        // 方式2：Set
        Set<String> addIncludes = new HashSet<>(Arrays.asList("name", "age", "email"));
        System.out.println("新增(Set): " + SimpleAuditUtil.getAddData(newUser, addIncludes));

        // 包含父类字段
        Set<String> addWithParent = new HashSet<>(Arrays.asList("id", "name", "age"));
        System.out.println("新增(包含父类id): " + SimpleAuditUtil.getAddData(newUser, addWithParent));

        // ========== 更新日志 - 使用 Set ==========
        System.out.println("\n=== 更新日志 ===");

        // 方式1：可变参数
        System.out.println("更新(可变参数): " + SimpleAuditUtil.getUpdateData(oldUser, updatedUser, "age", "email"));

        // 方式2：Set
        Set<String> updateIncludes = new HashSet<>(Arrays.asList("age", "email"));
        System.out.println("更新(Set): " + SimpleAuditUtil.getUpdateData(oldUser, updatedUser, updateIncludes));

        // 包含父类字段（id不变，不会显示）
        Set<String> updateWithParent = new HashSet<>(Arrays.asList("id", "name", "age", "email"));
        System.out.println("更新(包含父类id): " + SimpleAuditUtil.getUpdateData(oldUser, updatedUser, updateWithParent));

        // ========== 删除日志 - 使用 Set ==========
        System.out.println("\n=== 删除日志 ===");

        // 方式1：可变参数
        System.out.println("删除(可变参数): " + SimpleAuditUtil.getDeleteData(newUser, "id", "name", "age"));

        // 方式2：Set
        Set<String> deleteIncludes = new HashSet<>(Arrays.asList("id", "name", "age"));
        System.out.println("删除(Set): " + SimpleAuditUtil.getDeleteData(newUser, deleteIncludes));

        // ========== 通用方法 - 使用 Set ==========
        System.out.println("\n=== 通用方法 ===");

        Set<String> commonIncludes = new HashSet<>(Arrays.asList("name", "age", "email"));
        System.out.println("通用(Set): " + SimpleAuditUtil.getChangeData(oldUser, updatedUser, commonIncludes));

        // ========== 空值处理 ==========
        System.out.println("\n=== 空值处理 ===");
        System.out.println("null实体: " + SimpleAuditUtil.getAddData(null));
        System.out.println("空Set: " + SimpleAuditUtil.getAddData(newUser, new HashSet<String>()));
        System.out.println("null Set: " + SimpleAuditUtil.getAddData(newUser, (Set<String>) null));
    }
}
