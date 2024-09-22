package com.hodoo.hodoomall.coupon.model.dto;

import lombok.Data;

@Data
public class QueryDTO {

    private String name;
    private int page = 1;
    private int pageSize = 10;
    private boolean isDeleted = false;

}
