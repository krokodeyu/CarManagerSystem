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