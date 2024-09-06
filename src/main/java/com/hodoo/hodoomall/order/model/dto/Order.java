package com.hodoo.hodoomall.order.model.dto;

import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @DBRef
    private User user;

    private String status = "preparing";
    private double totalPrice;

    private ShipTo shipTo;
    private Contact contact;
    private String orderNum;

    private List<OrderItem> items;


    public static class ShipTo{
        private String address;
        private String city;
        private String zip;
    }

    public static class Contact{
        private String firstName;
        private String lastName;
        private String contact;
    }

    public static class OrderItem{

        @Id
        private String id;

        @DBRef
        private Product product;
        private double price;
        private int qty;
        private String size;
    }
}
