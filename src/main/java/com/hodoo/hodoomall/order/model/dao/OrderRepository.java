package com.hodoo.hodoomall.order.model.dao;

import com.hodoo.hodoomall.order.model.dto.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
