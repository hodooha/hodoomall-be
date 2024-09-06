package com.hodoo.hodoomall.cart.model.dao;

import com.hodoo.hodoomall.cart.model.dto.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
}
