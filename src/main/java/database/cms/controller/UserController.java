package database.cms.controller;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.DTO.request.UserUpdateRequest;
import database.cms.DTO.response.NotificationResponse;
import database.cms.DTO.response.NotificationsResponse;
import database.cms.DTO.response.RegisterResponse;
import database.cms.DTO.response.UserInfoResponse;
import database.cms.service.SecurityService;
import database.cms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #userId)")
    public ResponseEntity<UserInfoResponse> getUserInfo(
                                @PathVariable Long userId) {
        UserInfoResponse response = userService.getInfo(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<UserInfoResponse> responses = userService.getAllUsers();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody UserRegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #userId)")
    public ResponseEntity<UserInfoResponse> updateUser(
            @RequestBody UserUpdateRequest request,
            @PathVariable Long userId
    ) {
        UserInfoResponse response = userService.update(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-notification")
    public ResponseEntity<NotificationsResponse> checkNotification(Authentication authentication) {
        NotificationsResponse response = userService.checkNotification(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
