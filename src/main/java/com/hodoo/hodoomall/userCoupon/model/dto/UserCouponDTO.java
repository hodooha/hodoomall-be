package com.hodoo.hodoomall.userCoupon.model.dto;

import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "userCoupons")
public class UserCouponDTO {

    private String id;
    private ObjectId couponId;
    private CouponDTO coupon;
    private ObjectId userId;

    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    private LocalDate expiredAt;
    private int duration;

    private boolean isUsed = false;

    public UserCoupon toEntity(){
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponId(this.couponId);
        userCoupon.setUserId(this.userId);
        userCoupon.setUsedAt(this.usedAt);
        userCoupon.setExpiredAt(LocalDate.now().plusDays(this.duration));
        userCoupon.setUsed(this.isUsed);
        return userCoupon;
    }

    public UserCouponDTO(UserCoupon userCoupon){
        this.id = userCoupon.getId();
        this.couponId = userCoupon.getCouponId();
        this.userId = userCoupon.getUserId();
        this.createdAt = userCoupon.getCreatedAt();
        this.usedAt = userCoupon.getUsedAt();
        this.expiredAt = userCoupon.getExpiredAt();
        this.isUsed = userCoupon.isUsed();
    }
}
