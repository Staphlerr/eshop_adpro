package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.order = order;
        this.status = PaymentStatus.PENDING.getValue();
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData, Order order) {
        this(id, method, paymentData, order);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;

        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            this.order.setStatus("SUCCESS");
        } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
            this.order.setStatus("FAILED");
        }
    }
}
