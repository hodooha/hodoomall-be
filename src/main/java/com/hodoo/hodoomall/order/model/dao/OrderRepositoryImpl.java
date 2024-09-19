package com.hodoo.hodoomall.order.model.dao;

import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

        if(queryDTO.getUser() != null && !queryDTO.getUser().getLevel().equals("admin")){
            query.addCriteria(Criteria.where("userId").is(new ObjectId(queryDTO.getUser().getId())));
        }

        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page-1) * pageSize).limit(pageSize);

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

        if(queryDTO.getUser() != null && !queryDTO.getUser().getLevel().equals("admin")){
            query.addCriteria(Criteria.where("userId").is(new ObjectId(queryDTO.getUser().getId())));
        }

        return mongoTemplate.count(query, Order.class);
    }
}
