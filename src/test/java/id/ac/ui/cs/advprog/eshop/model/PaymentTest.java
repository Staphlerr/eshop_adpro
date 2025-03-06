package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class PaymentTest {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    @BeforeEach
    void setUp() {
        this.id = "payment1";
        this.method = "VOUCHER";
        this.status = "PENDING";

        this.paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(1);
        products.add(product1);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreatePaymentWithEmptyData() {
        this.paymentData.clear();

        Payment payment = new Payment(id, method, status, this.paymentData, order);

        assertEquals(id, payment.getId());
        assertEquals(method, payment.getMethod());
        assertEquals(status, payment.getStatus());
        assertEquals(order, payment.getOrder());
        assertTrue(payment.getPaymentData().isEmpty());
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment(id, method, status, this.paymentData, order);

        assertEquals(id, payment.getId());
        assertEquals(method, payment.getMethod());
        assertEquals(status, payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
        assertSame(this.order, payment.getOrder());
        assertEquals("Sampo Cap Bambang", payment.getOrder().getProducts().get(0).getProductName());
        assertEquals("13652556-012a-4c07-b546-54eb1396d79b", payment.getOrder().getId());
        assertEquals(1708560000L,payment.getOrder().getOrderTime());
        assertEquals("Safira Sudrajat", payment.getOrder().getAuthor());
    }

    @Test
    void testCreatePaymentSuccessStatus() {
        Payment payment = new Payment(id, method, "SUCCESS", this.paymentData, order);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    id, method, "MEOW", this.paymentData, order
            );
        });
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(id, method, status, this.paymentData, order);
        payment.setStatus("REJECTED");

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment(id, method, status, this.paymentData, order);

        assertThrows(IllegalArgumentException.class, () ->
                payment.setStatus("MEOW")
        );
    }

    @Test
    void testModifyPaymentData() {
        Payment payment = new Payment(id, "BANK_TRANSFER", status, new HashMap<>(), order);

        payment.getPaymentData().put("bankName", "XYZ Bank");

        assertEquals("XYZ Bank", payment.getPaymentData().get("bankName"));
    }
}
