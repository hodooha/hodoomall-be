package com.hodoo.hodoomall.cart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private ObjectId userId;

    private List<CartItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem{

        private ObjectId productId;
        private String size;
        private int qty = 1;

    }
}
