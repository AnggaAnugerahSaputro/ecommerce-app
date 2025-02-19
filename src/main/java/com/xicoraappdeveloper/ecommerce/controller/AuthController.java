package com.xicoraappdeveloper.ecommerce.controller;

import com.xicoraappdeveloper.ecommerce.model.UserInfo;
import com.xicoraappdeveloper.ecommerce.model.dto.request.AuthRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.request.UserRegisterRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.response.AuthResponse;
import com.xicoraappdeveloper.ecommerce.model.dto.response.UserResponse;
import com.xicoraappdeveloper.ecommerce.service.UserService;
import com.xicoraappdeveloper.ecommerce.service.security.AuthService;
import com.xicoraappdeveloper.ecommerce.service.security.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest authRequest
    ) {
        UserInfo userInfo = authService.authenticate(authRequest);
        String token = jwtService.generateToken(userInfo);
        AuthResponse authResponse = AuthResponse.fromUserInfo(userInfo, token);

        return ResponseEntity.ok(authResponse);
    }

    // POST /api/v1/auth/register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegisterRequest registerRequest
    ) {
        UserResponse userResponse = userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponse);
    }
}

//{
//        "username": "anugerah",
//        "email": "anugerah@gmail.com",
//        "password": "@nugerahS123",
//        "password_confirmation": "@nugerahS123"
//        }

//{
//        "username": "anggaanugerahs",
//        "email": "anggaanugerah@gmail.com",
//        "newPassword": "@nggaAS123",
//        "currentPassword": "@nggaAS123"
//        }