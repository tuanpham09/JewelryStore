package org.example.trangsuc_js.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // ✅ Cho phép login & register
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**", "/api/search/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/search/**").permitAll() // ✅ Cho phép POST search
                        .requestMatchers("/api/test/**").permitAll() // ✅ Cho phép test endpoints
                        .requestMatchers("/api/cart/test").permitAll() // ✅ Cho phép test cart endpoint
                        .requestMatchers("/api/payment/webhook").permitAll() // ✅ Cho phép webhook PayOS
                        .requestMatchers("/api/payment/confirm-webhook").permitAll() // ✅ Cho phép confirm webhook
                        .requestMatchers("/api/payment/create-payment-link").permitAll() // ✅ Cho phép tạo payment link
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/cart/**").authenticated() // ✅ Yêu cầu authentication cho cart
                        .requestMatchers("/api/payment/**").authenticated() // ✅ Yêu cầu authentication cho payment
                        .requestMatchers("/api/order/**").authenticated() // ✅ Yêu cầu authentication cho order
                        .requestMatchers("/api/order-history/**").authenticated() // ✅ Yêu cầu authentication cho order history
                        .requestMatchers("/api/profile/**").authenticated() // ✅ Yêu cầu authentication cho profile
                        .requestMatchers("/api/wishlist/**").authenticated() // ✅ Yêu cầu authentication cho wishlist
                        .requestMatchers("/api/search/**").authenticated() // ✅ Yêu cầu authentication cho search
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
