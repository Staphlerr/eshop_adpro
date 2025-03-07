package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "CreateOrder";
    }

    @GetMapping("/history")
    public String showHistoryForm() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String showOrderHistory(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String showPaymentPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);

        if (order == null) {
            return "redirect:/order/history";
        }

        model.addAttribute("order", order);
        return "order/pay";
    }

    @PostMapping("/pay/{orderId}")
    public String processPayment(@PathVariable String orderId, @RequestParam String method,
                                 @RequestParam Map<String, String> paymentData, Model model) {
        Order order = orderService.findById(orderId);

        if (order == null) {
            return "redirect:/order/history";
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("paymentId", payment.getId());
        return "order/pay-success";
    }
}
