package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "order-detail";
        }
        return "redirect:/orders";
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        List<CartItem> cartItems = cartService.getAllItems();
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        Order order = new Order();
        model.addAttribute("order", order);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "order-form";
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order, Model model) {
        List<CartItem> cartItems = cartService.getAllItems();

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng trống!");
            return "order-form";
        }

        // Thêm items từ giỏ hàng vào đơn hàng
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getProductId(),
                    cartItem.getProductName(),
                    cartItem.getPrice(),
                    cartItem.getQuantity(),
                    cartItem.getImage()
            );
            order.addItem(orderItem);
        }

        // Tính tổng tiền
        double totalPrice = orderService.calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);

        // Lưu đơn hàng
        Order savedOrder = orderService.createOrder(order);

        // Xóa giỏ hàng sau khi tạo đơn hàng
        cartService.clear();

        return "redirect:/orders/" + savedOrder.getId();
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            Model model) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/email/{email}")
    public String getOrdersByEmail(@PathVariable String email, Model model) {
        List<Order> orders = orderService.getOrdersByEmail(email);
        model.addAttribute("orders", orders);
        model.addAttribute("email", email);
        return "order-list-by-email";
    }
}
