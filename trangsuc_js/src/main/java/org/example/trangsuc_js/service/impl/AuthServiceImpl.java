package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.dto.auth.UserDto;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.repository.RoleRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.service.AuthService;
import org.example.trangsuc_js.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public TokenResponse register(RegisterRequest req) {
        // Kiểm tra email đã tồn tại
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo user mới
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());

        // Lấy role từ database
        var userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
                
        // Sử dụng phương thức addRole để thiết lập mối quan hệ đúng cách
        u.addRole(userRole);
        
        // Lưu user với role
        u = userRepo.save(u);
        
        // Lấy danh sách role name
        List<String> roles = u.getRoles().stream()
                .map(r -> r.getName())
                .toList();

        // Tạo token với email + roles
        String token = jwtUtil.generateToken(u.getEmail(), roles);

        // Trả về response có token
        return new TokenResponse(token);
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
    
    /**
     * Lấy thông tin người dùng hiện tại từ token
     */
    @Override
    public UserDto getCurrentUser(String token) {
        // Lấy email từ token
        String email = jwtUtil.getUsername(token);
        
        // Tìm user theo email
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        // Lấy danh sách role name
        List<String> roles = u.getRoles().stream()
                .map(r -> r.getName())
                .toList();
                
        // Chuyển thành DTO
        return new UserDto(
            u.getId(),
            u.getEmail(),
            u.getFullName(),
            roles
        );
    }
}
