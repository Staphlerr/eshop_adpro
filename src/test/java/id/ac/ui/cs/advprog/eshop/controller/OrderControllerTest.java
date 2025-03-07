package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    private Order mockOrder;
    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        mockOrder = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products,
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
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(Collections.singletonList(mockOrder));

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"))
                .andExpect(model().attribute("orders", Collections.singletonList(mockOrder)));

        verify(orderService, times(1)).findAllByAuthor("Safira Sudrajat");
    }

    @Test
    void testShowPaymentPage() throws Exception {
        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn(mockOrder);

        mockMvc.perform(get("/order/pay/13652556-012a-4c07-b546-54eb1396d79b"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay"))
                .andExpect(model().attribute("order", mockOrder));

        verify(orderService, times(1)).findById("13652556-012a-4c07-b546-54eb1396d79b");
    }

    @Test
    void testShowPaymentPage_NonExistingOrder() {
        String orderId = "nonExistingId";
        when(orderService.findById(orderId)).thenReturn(null);

        String viewName = orderController.showPaymentPage(orderId, model);

        verify(model, never()).addAttribute(eq("order"), any());
        assertEquals("redirect:/order/history", viewName);
        verify(orderService, times(1)).findById(orderId);
    }

    @Test
    void testProcessPayment() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("method", "VOUCHER");
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment mockPayment = new Payment("payment123",
                "VOUCHER", paymentData, mockOrder);

        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn(mockOrder);
        when(paymentService.addPayment(mockOrder, "VOUCHER", paymentData)).thenReturn(mockPayment);

        mockMvc.perform(post("/order/pay/13652556-012a-4c07-b546-54eb1396d79b")
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay-success"))
                .andExpect(model().attribute("paymentId", "payment123"));

        verify(paymentService, times(1)).addPayment(mockOrder, "VOUCHER", paymentData);
    }
}
