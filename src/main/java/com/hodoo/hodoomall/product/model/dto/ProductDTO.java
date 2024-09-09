package com.hodoo.hodoomall.product.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDTO {

//    private String id;
    private String sku;
    private String name;
    private String image;
    private List<String> category;
    private String description;
    private int price;
    private Map<String, Integer> stock;
    private String status = "active";
    private boolean isDeleted = false;

}
