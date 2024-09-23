package com.hodoo.hodoomall.userCoupon.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
public class QueryDTO {

    private ObjectId userId;
    private ObjectId couponId;
    private LocalDate expiredAt;
    private int page = 1;
    private int pageSize = 4;
}
