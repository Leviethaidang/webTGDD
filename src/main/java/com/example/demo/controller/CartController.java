package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getAllItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("totalQuantity", cartService.getTotalQuantity());
        return "cart-view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(productId).orElse(null);
        
        if (product != null) {
            cartService.addItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage()
            );
            redirectAttributes.addFlashAttribute("message", "Đã thêm '" + product.getName() + "' vào giỏ hàng");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại");
        }
        
        return "redirect:/products";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        cartService.removeItem(productId);
        redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng");
        return "redirect:/cart";
    }

    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Cập nhật số lượng thành công");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        cartService.clear();
        redirectAttributes.addFlashAttribute("message", "Giỏ hàng đã được làm trống");
        return "redirect:/products";
    }
}
