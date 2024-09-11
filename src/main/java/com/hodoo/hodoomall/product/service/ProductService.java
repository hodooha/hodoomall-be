package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws Exception;

    List<ProductDTO> getProductList(QueryDTO query) throws Exception;

    long getTotalProductCount(QueryDTO queryDTO) throws Exception;

    ProductDTO updateProduct(ProductDTO productDTO) throws Exception;
}
