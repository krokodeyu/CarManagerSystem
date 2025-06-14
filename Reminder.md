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

2. lrz:  完善测试stat（事务测试），vehicle和tech测试，催单功能

3. 弃用adminController, 改为statController

stat高级功能中期提交后再做

## 6/13
1. After user's publish, admin needs to arrange certain technicians. Relevant technicians need to confirm or reject the order.
If rejection occurs, admin needs to rearrange until all things are done.
2. Add notification to technicians, admin and users. 
3. Admin login 
4. JWT revision 
5. Test

## 6/14
1. AppointmentService-confirmAppointment need to be completed after the implementation of ADMIN.