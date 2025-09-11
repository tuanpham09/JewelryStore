package org.example.trangsuc_js.controller;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

}
