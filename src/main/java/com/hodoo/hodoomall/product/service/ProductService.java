package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws Exception;

    List<ProductDTO> getProductList() throws Exception;
}
