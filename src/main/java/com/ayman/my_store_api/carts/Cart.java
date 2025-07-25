package com.ayman.my_store_api.carts;

import com.ayman.my_store_api.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created",insertable = false,updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart" , cascade = CascadeType.MERGE, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice ()
    {
        return items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public CartItem getItem(long productId)
    {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
    }
    public CartItem addItem(Product product)
    {
        var cartItem = getItem(product.getId());
        if (cartItem!=null)
            cartItem.setQuantity(cartItem.getQuantity()+1);
        else
        {
            cartItem = new CartItem();
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItem.setProduct(product);
            items.add(cartItem);
        }
        return cartItem;
    }
    public void removeItem(long productId)
    {
        var cartItem = getItem(productId);
        if (cartItem!=null)
        {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clear()
    {
        items.clear();
    }
    public boolean isEmpty ()
    {
        return items.isEmpty();
    }
}