package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.id = "a2c47718-4b37-4664-81a7-f41bb87255";
        this.method = "VOUCHER_CODE";
        this.paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);

        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);

        products.add(product1);
        products.add(product2);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                this.products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment(id, method, this.paymentData, order);

        assertEquals(id, payment.getId());
        assertEquals(method, payment.getMethod());
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
        assertSame(this.order, payment.getOrder());
        assertEquals("Sampo Cap Bambang", payment.getOrder().getProducts().get(0).getProductName());
        assertEquals("Sabun Cap Usep", payment.getOrder().getProducts().get(1).getProductName());
        assertEquals("13652556-012a-4c07-b546-54eb1396d79b", payment.getOrder().getId());
        assertEquals(1708560000L,payment.getOrder().getOrderTime());
        assertEquals("Safira Sudrajat", payment.getOrder().getAuthor());
    }

    @Test
    void testCreatePaymentSuccessStatus() {
        Payment payment = new Payment(id, method, PaymentStatus.SUCCESS.getValue(), this.paymentData, order);

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
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
    void testSetStatusToSuccess() {
        Payment payment = new Payment(id, method, PaymentStatus.PENDING.getValue(), this.paymentData, order);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(id, method, PaymentStatus.PENDING.getValue(), this.paymentData, order);
        payment.setStatus(PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }
}
