package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;

public interface AuthService {
    void register(RegisterRequest req);
    TokenResponse login(LoginRequest req);
}

