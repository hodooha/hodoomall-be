package com.hodoo.hodoomall.coupon.model.dao;

import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;

import java.util.List;

public interface CouponRepositoryCustom {
    List<Coupon> findByQuery(QueryDTO queryDTO);

    long getTotalCouponCount(QueryDTO queryDTO);
}
