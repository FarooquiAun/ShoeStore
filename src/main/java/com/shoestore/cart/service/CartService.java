package com.shoestore.cart.service;

import com.shoestore.auth.entity.User;
import com.shoestore.auth.repository.UserRepository;
import com.shoestore.auth.security.CustomUserDetails;
import com.shoestore.cart.dto.AddToCartRequest;
import com.shoestore.cart.dto.CartItemResponse;
import com.shoestore.cart.dto.CartResponse;
import com.shoestore.cart.entity.Cart;
import com.shoestore.cart.entity.CartItem;
import com.shoestore.cart.repository.CartRepository;
import com.shoestore.common.util.SecurityUtil;
import com.shoestore.common.exceptions.ResourceNotFoundException;
import com.shoestore.product.entity.Shoe;
import com.shoestore.product.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ShoeRepository shoeRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ShoeRepository shoeRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.shoeRepository = shoeRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void addToCart( AddToCartRequest request){
        User user=getCurrentUser();
        Shoe shoe=shoeRepository.findById(request.getShoeId()).orElseThrow(
                ()-> new ResourceNotFoundException("Shoe Not found")
        );
        Cart cart=cartRepository.findByUser(user).orElseGet(
                ()->{
                    Cart newCart=new Cart(user);
                    return cartRepository.save(newCart);
                }
        );
        CartItem item=cart.getItems().stream().
                filter(i-> i.getShoe().getId()==(shoe.getId()))
                .findFirst().orElse(null);

        if(item==null){
            cart.getItems().add(new CartItem(cart,request.getQuantity(),shoe));
        }else {
            item.setQuantity(item.getQuantity()+request.getQuantity());
        }
        cartRepository.save(cart);
    }
    public CartResponse viewCart(){
        User user=getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart empty"));

        CartResponse response = new CartResponse();

        var items=cart.getItems().stream().map(item->{
            CartItemResponse r=new CartItemResponse();
            r.setItemId(item.getId());
            r.setQuantity(item.getQuantity());
            r.setShoeId(item.getShoe().getId());
            r.setPrice(item.getShoe().getPrice());
            r.setShoeName(item.getShoe().getName());
            return  r;
                }
        ).collect(Collectors.toList());
        response.setItems(items);
        BigDecimal total=items.stream().map(
                i-> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
        ).reduce(BigDecimal.ZERO,BigDecimal::add);
        response.setTotalAmount(total);
        return response;
    }
    private User getCurrentUser() {
        CustomUserDetails userDetails = SecurityUtil.getCurrentUserDetails();
        System.out.println("JWT USER ID = " + userDetails.getUserId());
        return userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
