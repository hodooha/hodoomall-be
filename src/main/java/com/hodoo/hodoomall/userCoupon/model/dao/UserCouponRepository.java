package com.hodoo.hodoomall.userCoupon.model.dao;

import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserCouponRepository extends MongoRepository<UserCoupon, String>, UserCouponRepositoryCustom {

    List<UserCoupon> findAllByCouponIdAndUserId(ObjectId couponId, ObjectId userId);

    List<UserCoupon> findAllByUserIdAndIsUsed(ObjectId userId, Boolean isUsed);

    void deleteByCouponId(String couponId);
}
