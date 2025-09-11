package org.example.trangsuc_js.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final Key key;
    private final long EXPIRATION = 1000 * 60 * 60; // 1 giờ

    public JwtUtil() {
        // key tối thiểu 256bit cho HS256
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // ✅ Sinh token với email + role
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", String.join(",", roles)) // lưu nhiều roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // ✅ Lấy username/email từ token
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Lấy roles từ token
    public String getRoles(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class);
    }

    // ✅ Validate token (chỉ cần 1 tham số)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
