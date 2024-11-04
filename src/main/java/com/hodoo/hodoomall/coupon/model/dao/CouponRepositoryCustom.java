package com.hodoo.hodoomall.coupon.model.dao;

import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;
import org.bson.types.ObjectId;

import java.util.List;

public interface CouponRepositoryCustom {
    List<Coupon> findByQuery(QueryDTO queryDTO);

    long getTotalCouponCount(QueryDTO queryDTO);

    Coupon minusCouponQty(ObjectId couponId) throws Exception;
}
