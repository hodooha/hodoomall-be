package com.hodoo.hodoomall.order.model.dao;

import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByQuery(QueryDTO queryDTO);

    long getTotalOrderCount(QueryDTO queryDTO);
}
