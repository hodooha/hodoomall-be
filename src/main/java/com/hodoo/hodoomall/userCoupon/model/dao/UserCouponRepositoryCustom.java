package com.hodoo.hodoomall.userCoupon.model.dao;

import com.hodoo.hodoomall.userCoupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;

import java.util.List;

public interface UserCouponRepositoryCustom {
    List<UserCoupon> findByQuery(QueryDTO queryDTO);

    UserCoupon useUserCoupon(String userCouponId);
}
