package com.hodoo.hodoomall.product.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank
    private String sku;

    @NotBlank
    private String name;

    private String image;

    @NotBlank
    private List<String> category;

    @NotNull
    private String description;

    @NotNull
    private int price;

    @NotNull
    private Map<String, Integer> stock;

    @NotBlank
    private String status = "active";

    @NotBlank
    private boolean isDeleted = false;


}
