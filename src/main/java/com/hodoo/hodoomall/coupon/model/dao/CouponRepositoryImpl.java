package com.hodoo.hodoomall.coupon.model.dao;

import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Coupon> findByQuery(QueryDTO queryDTO) {

        Query query = new Query();

        if(queryDTO.getName() != null && !queryDTO.getName().isEmpty()){
            query.addCriteria(Criteria.where("name").regex(queryDTO.getName(), "i"));
        }

        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page-1) * pageSize).limit(pageSize);

        return mongoTemplate.find(query, Coupon.class);
    }

    @Override
    public long getTotalCouponCount(QueryDTO queryDTO) {

        Query query = new Query();

        if(queryDTO.getName() != null && !queryDTO.getName().isEmpty()){
            query.addCriteria(Criteria.where("name").regex(queryDTO.getName(), "i"));
        }

        return mongoTemplate.count(query, Coupon.class);
    }

}
