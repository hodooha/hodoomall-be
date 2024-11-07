package com.hodoo.hodoomall.product.service;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;
import com.hodoo.hodoomall.product.model.dto.StockCheckResultDTO;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws Exception;

    List<ProductDTO> getProductList(QueryDTO query) throws Exception;

    long getTotalProductCount(QueryDTO queryDTO) throws Exception;

    void updateProduct(ProductDTO productDTO) throws Exception;

    void deleteProduct(String id) throws Exception;

    ProductDTO getProductDetail(String id) throws Exception;

//    List<StockCheckResultDTO> checkItemListStock(List<OrderDTO.OrderItemDTO> items) throws Exception;

    StockCheckResultDTO checkAndUpdateStock(OrderDTO.OrderItemDTO i) throws Exception;
}
