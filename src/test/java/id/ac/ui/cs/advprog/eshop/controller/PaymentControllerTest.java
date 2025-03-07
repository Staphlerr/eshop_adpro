package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
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
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController paymentController;

    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        mockPayment = new Payment(UUID.randomUUID().toString(), "VOUCHER",
                PaymentStatus.PENDING.getValue(), Map.of("voucherCode", "ESHOP1234ABC5678"), order);
    }


    @Test
    void testShowPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"));
    }

    @Test
    void testShowPaymentDetailById() throws Exception {
        when(paymentService.getPayment(mockPayment.getId())).thenReturn(mockPayment);

        mockMvc.perform(get("/payment/detail/" + mockPayment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"))
                .andExpect(model().attribute("payment", mockPayment));

        verify(paymentService, times(1)).getPayment(mockPayment.getId());
    }

    @Test
    void testShowAllPayments() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Collections.singletonList(mockPayment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/admin-list"))
                .andExpect(model().attribute("payments", Collections.singletonList(mockPayment)));

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testShowAdminPaymentDetail() throws Exception {
        when(paymentService.getPayment(mockPayment.getId())).thenReturn(mockPayment);

        mockMvc.perform(get("/payment/admin/detail/" + mockPayment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/admin-detail"))
                .andExpect(model().attribute("payment", mockPayment));

        verify(paymentService, times(1)).getPayment(mockPayment.getId());
    }

    @Test
    void testSetPaymentStatus() throws Exception {
        String paymentId = "a2c47718-4b37-4664-81a7-f41bb87255";

        when(paymentService.getPayment(paymentId)).thenReturn(mockPayment);

        mockMvc.perform(post("/payment/admin/set-status/" + paymentId)
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));

        verify(paymentService, times(1)).getPayment(paymentId);
        verify(paymentService, times(1)).setStatus(mockPayment, PaymentStatus.SUCCESS.getValue());
    }

}
