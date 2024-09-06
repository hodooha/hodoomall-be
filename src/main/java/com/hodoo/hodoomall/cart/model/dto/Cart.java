package com.hodoo.hodoomall.cart.model.dto;

import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    @DBRef
    private User user;

    private List<CartItem> items;

    public static class CartItem{

        @DBRef
        private Product product;

        private String size;
        private int qty = 1;

    }
}
