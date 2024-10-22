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
    private String type;
    private int dcAmount;
    private int minCost;
    private String status;
    private int duration;
    private String image;

    public Coupon toEntity(){
        Coupon coupon = new Coupon();
        coupon.setName(this.name);
        coupon.setDescription(this.description);
        coupon.setType(this.type);
        coupon.setDcAmount(this.dcAmount);
        coupon.setTotalQty(this.totalQty);
        coupon.setMinCost(this.minCost);
        coupon.setDuration(this.duration);
        coupon.setStatus(this.status);
        coupon.setImage(this.image);
        return coupon;
    }

    public CouponDTO(Coupon coupon){
        this.id = coupon.getId();
        this.type = coupon.getType();
        this.dcAmount = coupon.getDcAmount();
        this.name = coupon.getName();
        this.totalQty = coupon.getTotalQty();
        this.description = coupon.getDescription();
        this.minCost = coupon.getMinCost();
        this.duration = coupon.getDuration();
        this.status = coupon.getStatus();
        this.image = coupon.getImage();
    }

}
