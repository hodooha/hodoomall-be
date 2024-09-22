package com.hodoo.hodoomall.coupon.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponDTO {

    private String id;
    private String name;
    private int totalQty;
    private String description;
    private int dcRate;
    private int minCost;
    private int duration;

    public Coupon toEntity(){
        Coupon coupon = new Coupon();
        coupon.setName(this.name);
        coupon.setDescription(this.description);
        coupon.setDcRate(this.dcRate);
        coupon.setTotalQty(this.totalQty);
        coupon.setMinCost(this.minCost);
        coupon.setDuration(this.duration);
        return coupon;
    }

    public CouponDTO(Coupon coupon){
        this.id = coupon.getId();
        this.dcRate = coupon.getDcRate();
        this.name = coupon.getName();
        this.totalQty = coupon.getTotalQty();
        this.description = coupon.getDescription();
        this.minCost = coupon.getMinCost();
        this.duration = coupon.getDuration();
    }

}
