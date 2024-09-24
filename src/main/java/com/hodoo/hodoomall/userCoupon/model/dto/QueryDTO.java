package com.hodoo.hodoomall.userCoupon.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class QueryDTO {

    private ObjectId userId;
    private ObjectId couponId;
    private boolean isExpired = false;
    private boolean isUsed = false;
    private int page = 1;
    private int pageSize = 4;
}
