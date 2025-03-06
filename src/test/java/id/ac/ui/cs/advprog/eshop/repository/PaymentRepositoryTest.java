package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;
    Order order1, order2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products1 = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products1.add(product1);

        List<Product> products2 = new ArrayList<>();
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        products2.add(product2);

        this.order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products1, 1708560000L, "Safira Sudrajat");
        this.order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products2, 1708570000L, "Bambang Sudrajat");

        this.payments = new ArrayList<>();

        Map<String, String> paymentData1 = Map.of("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentData1, order1);
        payments.add(payment1);

        Map<String, String> paymentData2 = Map.of("XYZ Bank", "ABC1234567");
        Payment payment2 = new Payment("37f51234-128a-51c4-309f-c3132ba55",
                "BANK_TRANSFER", PaymentStatus.PENDING.getValue(), paymentData2, order2);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getOrder().getId(), findResult.getOrder().getId());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(0);
        paymentRepository.save(payment);
        Payment newPayment = new Payment(payment.getId(),
                payment.getMethod(), PaymentStatus.SUCCESS.getValue(),
                payment.getPaymentData(), payment.getOrder());
        Payment result = paymentRepository.save(newPayment);

        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
        assertEquals(payments.get(1).getOrder().getId(), findResult.getOrder().getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("unknown-id");
        assertNull(findResult);
    }

    @Test
    void testFindAllPayments() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
    }
}
