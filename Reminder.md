# 备忘录

## 1.身份认证相关

身份信息储存在entity.Role中，要使用身份信息，在需要身份验证的方法前加`@PreAuthorize("hasRole('ADMIN')")`
例如：

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/users")
public List<User> getAllUsers() {
    return userService.findAll();
}
```

## 2.工作流

1. yjh：feedback，appointment（重构为订单），parts， 建表语句，ER图

2. lrz:  完善测试stat（事务测试），vehicle和tech测试，

3. 弃用adminController, 改为statController

stat高级功能中期提交后再做