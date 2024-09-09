package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws Exception;
}
