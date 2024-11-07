package com.hodoo.hodoomall.product.model.dto;

import lombok.Data;

@Data
public class QueryDTO {

    private String name;
    private int page = 1;
    private String category;
    private String status = "active";
    private int pageSize = 5;

}
