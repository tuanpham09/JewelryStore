package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.dto.auth.UserDto;

public interface AuthService {
    TokenResponse register(RegisterRequest req);
    TokenResponse login(LoginRequest req);
    UserDto getCurrentUser(String token);
}

