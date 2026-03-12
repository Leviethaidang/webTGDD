package com.example.demo.service;

import com.example.demo.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import java.io.Serializable;
import java.util.*;

@Service
@SessionScope
public class CartService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<Long, CartItem> cartItems = new HashMap<>();

    public void addItem(Long productId, String productName, double price, String image) {
        CartItem item = cartItems.get(productId);
        
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
        } else {
            cartItems.put(productId, new CartItem(productId, productName, price, 1, image));
        }
    }

    public void removeItem(Long productId) {
        cartItems.remove(productId);
    }

    public void updateQuantity(Long productId, int quantity) {
        CartItem item = cartItems.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                removeItem(productId);
            } else {
                item.setQuantity(quantity);
            }
        }
    }

    public List<CartItem> getAllItems() {
        return new ArrayList<>(cartItems.values());
    }

    public CartItem getItem(Long productId) {
        return cartItems.get(productId);
    }

    public int getCartSize() {
        return cartItems.size();
    }

    public int getTotalQuantity() {
        return cartItems.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public double getTotalPrice() {
        return cartItems.values().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    public void clear() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
