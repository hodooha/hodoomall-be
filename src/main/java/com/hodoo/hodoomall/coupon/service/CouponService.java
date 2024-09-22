package com.hodoo.hodoomall.coupon.service;

import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;

import java.util.List;

public interface CouponService {
    void createCoupon(CouponDTO couponDTO) throws Exception;

    List<CouponDTO> getCouponList(QueryDTO queryDTO) throws Exception;

    long getTotalCouponCount(QueryDTO queryDTO) throws Exception;

    void deleteCoupon(String id) throws Exception;
}
