package com.hodoo.hodoomall.userCoupon.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "userCoupons")
public class UserCoupon {

    @Id
    private String id;

    @NotBlank
    private ObjectId couponId;

    @NotBlank
    private ObjectId userId;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    @NotBlank
    private LocalDate expiredAt;

    @NotBlank
    private boolean isUsed = false;
}
