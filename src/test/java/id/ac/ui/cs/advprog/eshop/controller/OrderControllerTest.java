package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        mockOrder = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                Collections.emptyList(),
                1708560000L,
                "Safira Sudrajat");
    }

    @Test
    void testShowCreateOrderForm() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testShowHistoryForm() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"));
    }

    @Test
    void testShowOrderHistory() throws Exception {
        when(orderService.getOrdersByAuthor("Safira Sudrajat")).thenReturn(Collections.singletonList(mockOrder));

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"))
                .andExpect(model().attribute("orders", Collections.singletonList(mockOrder)));

        verify(orderService, times(1)).getOrdersByAuthor("Safira Sudrajat");
    }

    @Test
    void testShowPaymentPage() throws Exception {
        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn(Optional.of(mockOrder));

        mockMvc.perform(get("/order/pay/13652556-012a-4c07-b546-54eb1396d79b"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay"))
                .andExpect(model().attribute("order", mockOrder));

        verify(orderService, times(1)).findById("13652556-012a-4c07-b546-54eb1396d79b");
    }

    @Test
    void testShowPaymentPage_NonExistingOrder() {
        String orderId = "nonExistingId";
        when(orderService.findById(orderId)).thenReturn(Optional.empty());

        String viewName = orderController.showPaymentPage(orderId, model);

        verify(model, never()).addAttribute(eq("order"), any());
        assertEquals("redirect:/order/history", viewName);
        verify(orderService, times(1)).findById(orderId);
    }

    @Test
    void testProcessPayment() throws Exception {
        when(orderService.processPayment("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn("payment123");

        mockMvc.perform(post("/order/pay/13652556-012a-4c07-b546-54eb1396d79b"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay-success"))
                .andExpect(model().attribute("paymentId", "payment123"));

        verify(orderService, times(1)).processPayment("13652556-012a-4c07-b546-54eb1396d79b");
    }
}
