package com.hodoo.hodoomall.coupon.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private int dcRate;
    @NotBlank
    private int minCost;
    @NotBlank
    private LocalDateTime expiryDate;


}
