package com.hodoo.hodoomall.userCoupon.service;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;

import java.util.List;

public interface UserCouponService {
    void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception;

    void createUserCoupon0(UserCouponDTO userCouponDTO) throws Exception;

    void createUserCoupon1(UserCouponDTO userCouponDTO) throws Exception;

    List<UserCouponDTO> getUserCouponList(User user) throws Exception;

    void useUserCoupon(String userCouponId) throws Exception;

    void checkUserCoupon(UserCouponDTO userCouponDTO) throws Exception;

    void createUserCoupon2(UserCouponDTO userCouponDTO) throws Exception;

    void verifyUserCoupon(OrderDTO data) throws Exception;
}
