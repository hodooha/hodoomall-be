package com.hodoo.hodoomall.order.service;

import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import com.hodoo.hodoomall.user.model.dto.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderDTO data) throws Exception;

    List<OrderDTO> getOrder(QueryDTO queryDTO) throws Exception;

    long getTotalOrderCount(QueryDTO queryDTO) throws Exception;

    void updateOrder(QueryDTO queryDTO) throws Exception;
}
