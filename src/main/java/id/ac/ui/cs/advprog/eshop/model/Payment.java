package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;
import java.util.Arrays;

@Getter
@Builder
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, String status, Map<String, String> paymentData, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.order = order;
        setStatus(status);
    }

    public void setStatus(String status) {
        String[] validStatuses = {"PENDING", "SUCCESS", "REJECTED"};
        if (Arrays.stream(validStatuses).noneMatch(valid -> valid.equals(status))) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
        if (status.equals("SUCCESS")) {
            this.order.setStatus("SUCCESS");
        } else if (status.equals("REJECTED")) {
            this.order.setStatus("FAILED");
        }
    }
}
