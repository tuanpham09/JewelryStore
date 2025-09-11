package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.repository.RoleRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.service.AuthService;
import org.example.trangsuc_js.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    /**
     * Xử lý đăng ký tài khoản mới
     */
    @Override
    public void register(RegisterRequest req) {
        // Kiểm tra email đã tồn tại
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo user mới
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());

        // Gán role mặc định
        u.getRoles().add(roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found")));

        userRepo.save(u);
    }

    /**
     * Xử lý đăng nhập, trả về token JWT
     */
    @Override
    public TokenResponse login(LoginRequest req) {
        // Tìm user theo email
        User u = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra mật khẩu
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Lấy danh sách role name (VD: ROLE_USER, ROLE_ADMIN)
        List<String> roles = u.getRoles().stream()
                .map(r -> r.getName())
                .toList();

        // Tạo token với email + roles
        String token = jwtUtil.generateToken(u.getEmail(), roles);

        // Trả về response có token
        return new TokenResponse(token);
    }
}
