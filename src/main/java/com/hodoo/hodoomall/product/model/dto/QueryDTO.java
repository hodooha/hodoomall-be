package com.hodoo.hodoomall.product.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryDTO {

    private String name;
    private int page;
    private List<String> category;
    private boolean isDeleted = false;
    private int pageSize = 5;

}
