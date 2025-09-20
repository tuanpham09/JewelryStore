package org.example.trangsuc_js.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trangsuc_js.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getServletPath();
        String method = request.getMethod();
        log.info("JWT Filter - Path: {}, Method: {}", path, method);
        
        String header = request.getHeader("Authorization");
        log.info("Authorization header: {}", header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            log.info("Token extracted: {}", token.substring(0, Math.min(20, token.length())) + "...");

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getUsername(token);
                String roles = jwtUtil.getRoles(token);
                log.info("Token valid - Email: {}, Roles: {}", email, roles);

                List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication set for user: {}", email);
            } else {
                log.warn("Token validation failed");
            }
        } else {
            log.warn("No valid Authorization header found");
        }

        chain.doFilter(request, response);
    }

    /**
     * ✅ Bỏ qua filter cho các endpoint auth (login, register) và test
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth") || path.startsWith("/api/test");
    }
}
