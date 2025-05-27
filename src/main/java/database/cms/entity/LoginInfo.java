package database.cms.entity;

import lombok.Data;

@Data
public class LoginInfo {
    private Long id;
    private String username;
    private Role role;

    public LoginInfo(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
