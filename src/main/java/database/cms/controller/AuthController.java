package database.cms.controller;

import database.cms.DTO.request.LoginRequest;
import database.cms.DTO.response.JWTResponse;
import database.cms.DTO.response.MessageResponse;
import database.cms.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
