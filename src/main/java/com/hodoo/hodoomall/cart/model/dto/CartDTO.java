package com.hodoo.hodoomall.cart.model.dto;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartDTO {

    private String id;
    private String userId;
    private List<CartItemDTO> items;

    @Data
    public static class CartItemDTO{

        private ProductDTO productId;
        private String size;
        private int qty = 1;

    }

}
