package com.hodoo.hodoomall.order.model.dao;

import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import com.hodoo.hodoomall.product.model.dto.Product;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final MongoTemplate mongoTemplate;


    @Override
    public List<Order> findByQuery(QueryDTO queryDTO) {

        Query query = new Query();

        if(queryDTO.getOrderNum() != null && !queryDTO.getOrderNum().isEmpty()){
            query.addCriteria(Criteria.where("orderNum").regex(queryDTO.getOrderNum(), "i"));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").regex(queryDTO.getStatus(), "i"));
        }

        if(queryDTO.getProductName() != null && !queryDTO.getProductName().isEmpty()){
            Query productQuery = new Query(Criteria.where("name").regex(queryDTO.getProductName(), "i"));
            List<ObjectId> products = mongoTemplate.find(productQuery, Product.class).stream().map(product -> new ObjectId(product.getId())).toList();
            query.addCriteria(Criteria.where("items.productId").in(products));
        }

        if(queryDTO.getUser() != null){
            query.addCriteria(Criteria.where("userId").is(new ObjectId(queryDTO.getUser().getId())));
        }

        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page-1) * pageSize).limit(pageSize);

        if(queryDTO.getSortBy() == null || queryDTO.getSortBy().equals("latest")){
            query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else{
            query.with(Sort.by(Sort.Direction.ASC, "createdAt"));
        }

        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public long getTotalOrderCount(QueryDTO queryDTO) {

        Query query = new Query();

        if(queryDTO.getOrderNum() != null && !queryDTO.getOrderNum().isEmpty()){
            query.addCriteria(Criteria.where("orderNum").regex(queryDTO.getOrderNum(), "i"));
        }

        if(queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()){
            query.addCriteria(Criteria.where("status").regex(queryDTO.getStatus(), "i"));
        }

        if(queryDTO.getProductName() != null && !queryDTO.getProductName().isEmpty()){
            Query productQuery = new Query(Criteria.where("name").regex(queryDTO.getProductName(), "i"));
            List<ObjectId> products = mongoTemplate.find(productQuery, Product.class).stream().map(product -> new ObjectId(product.getId())).toList();
            query.addCriteria(Criteria.where("items.productId").in(products));
        }

        if(queryDTO.getUser() != null){
            query.addCriteria(Criteria.where("userId").is(new ObjectId(queryDTO.getUser().getId())));
        }

        return mongoTemplate.count(query, Order.class);
    }

    @Override
    public Order updateOrder(QueryDTO queryDTO) {

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("_id").is(queryDTO.getId()));
        update.set("status", queryDTO.getStatus());

        return mongoTemplate.findAndModify(query, update, Order.class);
    }

    @Override
    public Order cancelOrder(QueryDTO queryDTO){

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("_id").is(queryDTO.getId()).and("status").is("preparing"));
        update.set("status", "canceled");

        return mongoTemplate.findAndModify(query, update, Order.class);
    }
}
