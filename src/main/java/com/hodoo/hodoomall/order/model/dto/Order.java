package com.hodoo.hodoomall.order.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @NotBlank
    private ObjectId userId;

    @NotBlank
    private String status = "preparing";
    @NotBlank
    private int totalPrice;
    @NotBlank
    private ShipTo shipTo;
    @NotBlank
    private Contact contact;
    @NotBlank
    private String orderNum;

    @NotBlank
    private List<OrderItem> items;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShipTo{
        private String address;
        private String city;
        private String zip;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact{
        private String firstName;
        private String lastName;
        private String contact;
    }

    @Data
    @NoArgsConstructor
    public static class OrderItem{

        @Id
        private String id;

        private ObjectId productId;
        private int price;
        private int qty;
        private String size;

        public OrderItem(ObjectId id, int price, int qty, String size){
            this.productId = id;
            this.price = price;
            this.qty = qty;
            this.size = size;
        }

    }
}
