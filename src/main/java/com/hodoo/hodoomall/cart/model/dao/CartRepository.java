package com.hodoo.hodoomall.cart.model.dao;

import com.hodoo.hodoomall.cart.model.dto.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String>, CartRepositoryCustom {
    Optional<Cart> findByUserId(ObjectId userId);
}
