package com.hodoo.hodoomall.product.model.dao;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.QueryDTO;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findByQuery(QueryDTO queryDTO);

    long getTotalProductCount(QueryDTO queryDTO);

    Product updateStock(OrderDTO.OrderItemDTO orderItemDTO);

}
