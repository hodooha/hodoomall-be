package com.hodoo.hodoomall.product.model.dao;

import com.hodoo.hodoomall.product.model.dto.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepositoty extends MongoRepository<Product, String> {
}
