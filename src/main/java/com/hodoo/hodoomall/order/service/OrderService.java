package com.hodoo.hodoomall.order.service;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.user.model.dto.User;

public interface OrderService {
    void createOrder(User user, OrderDTO data) throws Exception;
}
