package org.example.trangsuc_js.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trangsuc_js.dto.auth.LoginRequest;
import org.example.trangsuc_js.dto.auth.RegisterRequest;
import org.example.trangsuc_js.dto.auth.TokenResponse;
import org.example.trangsuc_js.dto.auth.UserDto;
import org.example.trangsuc_js.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private TokenResponse tokenResponse;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Chuẩn bị dữ liệu test
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFullName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        tokenResponse = new TokenResponse("test-jwt-token");

        userDto = new UserDto(1L, "test@example.com", "Test User", Arrays.asList("ROLE_USER"));
    }

    @Test
    void testRegister() throws Exception {
        when(authService.register(any(RegisterRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"));
    }

    @Test
    void testLogin() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"));
    }

    @Test
    void testGetCurrentUser() throws Exception {
        when(authService.getCurrentUser(Mockito.anyString())).thenReturn(userDto);

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer test-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User details retrieved"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.fullName").value("Test User"));
    }
}
