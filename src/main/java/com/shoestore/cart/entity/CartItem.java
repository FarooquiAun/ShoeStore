package com.shoestore.cart.entity;

import com.shoestore.product.entity.Shoe;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items",
     uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id","shoe_id"})
)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "shoe_id",nullable = false)
    private Shoe shoe;

    @Column(nullable = false)
    private int quantity;

    public CartItem() {
    }

    public CartItem(Cart cart, int quantity, Shoe shoe) {
        this.cart = cart;
        this.quantity = quantity;
        this.shoe = shoe;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }
}
