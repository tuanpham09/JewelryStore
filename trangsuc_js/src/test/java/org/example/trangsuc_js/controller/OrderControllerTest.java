package org.example.trangsuc_js.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trangsuc_js.dto.order.CheckoutDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.dto.order.OrderItemDto;
import org.example.trangsuc_js.service.OrderService;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderDto orderDto;
    private List<OrderDto> orderDtoList;
    private CheckoutDto checkoutDto;

    @BeforeEach
    void setUp() {
        // Chuẩn bị dữ liệu test
        List<OrderItemDto> items = Arrays.asList(
                new OrderItemDto(1L, "Nhẫn vàng", 1, new BigDecimal("1000000")),
                new OrderItemDto(2L, "Dây chuyền bạc", 2, new BigDecimal("1500000"))
        );
        
        orderDto = new OrderDto(1L, "PENDING", new BigDecimal("2500000"), LocalDateTime.now(), items);
        orderDtoList = Arrays.asList(orderDto);
        
        checkoutDto = new CheckoutDto();
        checkoutDto.setAddress("123 Test Street, Test City");
    }

    @Test
    @WithMockUser
    void testCheckout() throws Exception {
        when(orderService.checkout(any(CheckoutDto.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(checkoutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.total").value(2500000))
                .andExpect(jsonPath("$.items[0].productName").value("Nhẫn vàng"))
                .andExpect(jsonPath("$.items[1].productName").value("Dây chuyền bạc"));
    }

    @Test
    @WithMockUser
    void testMyOrders() throws Exception {
        when(orderService.myOrders()).thenReturn(orderDtoList);

        mockMvc.perform(get("/api/orders/mine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].total").value(2500000))
                .andExpect(jsonPath("$[0].items[0].productName").value("Nhẫn vàng"));
    }
}
