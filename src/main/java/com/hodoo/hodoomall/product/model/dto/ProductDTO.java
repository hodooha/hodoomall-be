package com.hodoo.hodoomall.product.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ProductDTO {

    private String id;
    private String sku;
    private String name;
    private String image;
    private List<String> category;
    private String description;
    private int price;
    private Map<String, Integer> stock;
    private String status = "active";
    private boolean isDeleted = false;

    public Product toEntity(){
        Product product = new Product();
        product.setSku(this.sku);
        product.setImage(this.image);
        product.setCategory(this.category);
        product.setStatus(this.status);
        product.setPrice(this.price);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setStock(this.stock);
        return product;
    }

    public ProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.category = product.getCategory();
        this.sku = product.getSku();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.isDeleted = product.isDeleted();
    }



}
