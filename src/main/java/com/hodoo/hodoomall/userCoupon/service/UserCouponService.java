package com.hodoo.hodoomall.userCoupon.service;

import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;

import java.util.List;

public interface UserCouponService {
    void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception;

    List<UserCouponDTO> getUserCouponList(User user) throws Exception;

    void useUserCoupon(String userCouponId) throws Exception;
}
