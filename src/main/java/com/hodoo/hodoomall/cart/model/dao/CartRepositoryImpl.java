package com.hodoo.hodoomall.cart.model.dao;

import com.hodoo.hodoomall.cart.model.dto.Cart;
import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteProductFromCart(String id) {
        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("items.productId").is(new ObjectId(id)));
        update.pull("items", new BasicDBObject("productId", new ObjectId(id)));

        mongoTemplate.updateMulti(query, update, Cart.class);
    }
}
