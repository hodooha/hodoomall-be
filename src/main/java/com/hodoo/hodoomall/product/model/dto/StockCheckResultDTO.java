package com.hodoo.hodoomall.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCheckResultDTO {

    private boolean isVerify;
    private String message;
}
