package org.example.trangsuc_js.controller;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.dto.auth.UserDto;
import org.example.trangsuc_js.service.AuthService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenResponse>> register(@RequestBody RegisterRequest request) {
        TokenResponse token = authService.register(request);
        return ResponseEntity.ok(new ApiResponse<>(
            true, 
            "Registration successful",
            token
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Login successful",
            token
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@RequestHeader("Authorization") String token) {
        UserDto user = authService.getCurrentUser(token.replace("Bearer ", ""));
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "User details retrieved",
            user
        ));
    }
}
