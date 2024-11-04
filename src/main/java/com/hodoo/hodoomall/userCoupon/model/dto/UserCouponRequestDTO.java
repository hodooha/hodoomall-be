package com.hodoo.hodoomall.userCoupon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCouponRequestDTO {

    private String couponId;
    private String userId;
    private int duration;
}
