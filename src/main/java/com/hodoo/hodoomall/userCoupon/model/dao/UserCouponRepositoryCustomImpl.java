package com.hodoo.hodoomall.userCoupon.model.dao;

import com.hodoo.hodoomall.userCoupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryCustomImpl implements UserCouponRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<UserCoupon> findByQuery(QueryDTO queryDTO) {

        Query query = new Query();
        if (queryDTO.getCouponId() != null) {
            query.addCriteria(Criteria.where("couponId").is(queryDTO.getCouponId()));
        }

        if (queryDTO.getUserId() != null) {
            query.addCriteria(Criteria.where("userId").is(queryDTO.getUserId()));
        }

        if (queryDTO.isExpired()) {
            query.addCriteria(Criteria.where("expiredAt").lte(LocalDate.now()));
        } else {
            query.addCriteria(Criteria.where("expiredAt").gt(LocalDate.now()));
        }

        if (queryDTO.isUsed()) {
            query.addCriteria(Criteria.where("isUsed").is(true));
        } else {
            query.addCriteria(Criteria.where("isUsed").is(false));
        }


        int page = queryDTO.getPage();
        int pageSize = queryDTO.getPageSize();
        query.skip((long) (page - 1) * pageSize).limit(pageSize);

        return mongoTemplate.find(query, UserCoupon.class);
    }

    @Override
    public UserCoupon useUserCoupon(String userCouponId){

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("_id").is(userCouponId).and("isUsed").is(false));
        query.addCriteria(Criteria.where("expiredAt").gte(LocalDate.now()));

        update.set("isUsed", true);
        update.currentDate("usedAt");

        return mongoTemplate.findAndModify(query, update, UserCoupon.class);
    }
}
