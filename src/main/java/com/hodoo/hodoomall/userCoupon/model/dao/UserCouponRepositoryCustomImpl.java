package com.hodoo.hodoomall.userCoupon.model.dao;

import com.hodoo.hodoomall.userCoupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryCustomImpl implements UserCouponRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<UserCoupon> findByQuery(QueryDTO queryDTO) {

        Query query = new Query();
        if(queryDTO.getCouponId() != null){
            query.addCriteria(Criteria.where("couponId").is(queryDTO.getCouponId()));
        }

        if(queryDTO.getUserId() != null){
            query.addCriteria(Criteria.where("userId").is(queryDTO.getUserId()));
        }

        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page -1) * pageSize).limit(pageSize);

        return mongoTemplate.find(query, UserCoupon.class);
    }
}
