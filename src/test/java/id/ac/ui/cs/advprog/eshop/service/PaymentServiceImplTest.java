package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order1, order2;
    Map<String, String> paymentDataVoucher;
    Map<String, String> paymentDataBank;

    @BeforeEach
    void setUp() {
        // Setup Order
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

        order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b", products1, 1708560000L, "Safira Sudrajat");
        order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078", products2, 1708570000L, "Bambang Sudrajat");

        // Setup PaymentData
        paymentDataVoucher = Map.of("voucherCode", "ESHOP1234ABC5678");
        paymentDataBank = Map.of("XYZ Bank", "ABC1234567");
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87255", "VOUCHER", PaymentStatus.SUCCESS.getValue(), paymentDataVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order1, "VOUCHER", paymentDataVoucher);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferSuccess() {
        Payment payment = new Payment("37f51234-128a-51c4-309f-c3132ba55", "BANK_TRANSFER", PaymentStatus.PENDING.getValue(), paymentDataBank, order2);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order2, "BANK_TRANSFER", paymentDataBank);

        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherRejected_InvalidLength() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "ESHOP1234ABCD"); // Kurang dari 16 karakter
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87256", "VOUCHER", PaymentStatus.REJECTED.getValue(), invalidVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order1, "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherRejected_NoESHOPPrefix() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "INVALID1234ABC5678"); // Tidak diawali "ESHOP"
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87257", "VOUCHER", PaymentStatus.REJECTED.getValue(), invalidVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order1, "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherRejected_No8Digits() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "ESHOPABCDEFGHJKLMN"); // Tidak ada 8 angka
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87258", "VOUCHER", PaymentStatus.REJECTED.getValue(), invalidVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order1, "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferRejected_EmptyBankName() {
        Map<String, String> invalidBankTransfer = Map.of("bankName", "", "referenceCode", "ABC1234567");
        Payment payment = new Payment("37f51234-128a-51c4-309f-c3132ba56", "BANK_TRANSFER", PaymentStatus.REJECTED.getValue(), invalidBankTransfer, order2);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order2, "BANK_TRANSFER", invalidBankTransfer);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferRejected_EmptyReferenceCode() {
        Map<String, String> invalidBankTransfer = Map.of("bankName", "XYZ Bank", "referenceCode", "");
        Payment payment = new Payment("37f51234-128a-51c4-309f-c3132ba57", "BANK_TRANSFER", PaymentStatus.REJECTED.getValue(), invalidBankTransfer, order2);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order2, "BANK_TRANSFER", invalidBankTransfer);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferRejected_BothEmpty() {
        Map<String, String> invalidBankTransfer = Map.of("bankName", "", "referenceCode", "");
        Payment payment = new Payment("37f51234-128a-51c4-309f-c3132ba58", "BANK_TRANSFER", PaymentStatus.REJECTED.getValue(), invalidBankTransfer, order2);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order2, "BANK_TRANSFER", invalidBankTransfer);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }


    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher, order1);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(PaymentStatus.FAILED.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher, order1);

        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW"));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher, order1);

        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findById(payment.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(
                new Payment("a2c47718-4b37-4664-81a7-f41bb87255",
                        "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher, order1),
                new Payment("37f51234-128a-51c4-309f-c3132ba55",
                        "BANK_TRANSFER", PaymentStatus.PENDING.getValue(), paymentDataBank, order2)
        );

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }
}
