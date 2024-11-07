package com.hodoo.hodoomall.coupon.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coupons")
@Data
public class Coupon {

    @Id
    private String id;

    @NotBlank
    private String name;
    @NotBlank
    private int totalQty;
    @NotBlank
    private String description;
    @NotBlank
    private String type;
    @NotBlank
    private int dcAmount;
    @NotBlank
    private int minCost;
    @NotBlank
    private int duration;

    private String image;

    @NotBlank
    private String status;


}
