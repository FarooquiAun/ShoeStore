package com.shoestore.cart.controller;



import com.shoestore.cart.dto.AddToCartRequest;
import com.shoestore.cart.dto.CartResponse;
import com.shoestore.cart.service.CartService;
import com.shoestore.auth.entity.User;
import com.shoestore.common.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public void addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse viewCart() {
        return cartService.viewCart();
    }
}

