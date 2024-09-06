package com.hodoo.hodoomall.product.model.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String sku;
    private String name;
    private String image;
    private List<String> category;
    private String description;
    private double price;
    private Stock stock;
    private String status = "active";
    private boolean isDeleted = false;

    public static class Stock{
        private String size;
        private int count;
    }

}
