package com.hodoo.hodoomall.coupon.model.dao;

import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponRepository extends MongoRepository<Coupon, String>, CouponRepositoryCustom {
}
