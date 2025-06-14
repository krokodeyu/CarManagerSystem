package database.cms.controller;

import database.cms.DTO.request.LoginRequest;
import database.cms.DTO.response.*;
import database.cms.detail.CustomUserDetails;
import database.cms.entity.Role;
import database.cms.service.AuthService;
import database.cms.service.TechnicianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TechnicianService technicianService;

    public AuthController(AuthService authService, TechnicianService technicianService) {
        this.authService = authService;
        this.technicianService = technicianService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> UserLogin(
            @RequestBody LoginRequest request
    ){
        JWTResponse response = authService.Login(request);
        if(response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("用户名或密码错误"));
        }
    }
}
