package org.example.trangsuc_js.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.CartItemDto;
import org.example.trangsuc_js.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private CartDto cartDto;
    private AddCartItemDto addCartItemDto;

    @BeforeEach
    void setUp() {
        // Chuẩn bị dữ liệu test
        List<CartItemDto> items = Arrays.asList(
                new CartItemDto(1L, "Nhẫn vàng", 1, new BigDecimal("1000000")),
                new CartItemDto(2L, "Dây chuyền bạc", 2, new BigDecimal("1500000"))
        );
        
        cartDto = new CartDto(items, new BigDecimal("2500000"));
        
        addCartItemDto = new AddCartItemDto();
        addCartItemDto.setProductId(3L);
        addCartItemDto.setQuantity(1);
    }

    @Test
    @WithMockUser
    void testGetMyCart() throws Exception {
        when(cartService.getMyCart()).thenReturn(cartDto);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productName").value("Nhẫn vàng"))
                .andExpect(jsonPath("$.items[1].productName").value("Dây chuyền bạc"))
                .andExpect(jsonPath("$.total").value(2500000));
    }

    @Test
    @WithMockUser
    void testAddItem() throws Exception {
        when(cartService.addItem(any(AddCartItemDto.class))).thenReturn(cartDto);

        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productName").value("Nhẫn vàng"))
                .andExpect(jsonPath("$.total").value(2500000));
    }

    @Test
    @WithMockUser
    void testRemoveItem() throws Exception {
        when(cartService.removeItem(anyLong())).thenReturn(cartDto);

        mockMvc.perform(delete("/api/cart/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productName").value("Nhẫn vàng"))
                .andExpect(jsonPath("$.total").value(2500000));
    }
}
