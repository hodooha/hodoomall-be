package com.hodoo.hodoomall.coupon.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "userCoupons")
public class UserCouponDTO {

    private String id;
    private ObjectId couponId;
    private ObjectId userId;

    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    private LocalDate expiredAt;

    private boolean isUsed = false;
}
