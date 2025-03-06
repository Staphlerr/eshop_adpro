package id.ac.ui.cs.advprog.eshop.model;

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

    public Payment(String id, String method, String status, Map<String, String> paymentData, Order order) {
    }

    public void setStatus(String status) {
    }
}
