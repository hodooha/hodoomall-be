package com.hodoo.hodoomall.userCoupon.service;

import com.hodoo.hodoomall.coupon.model.dao.CouponRepository;
import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dao.UserCouponRepository;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Override
    public void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception {

        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty())
            throw new Exception("이미 다운받은 쿠폰입니다.");

        UserCoupon userCoupon = userCouponDTO.toEntity();
        userCouponRepository.save(userCoupon);

    }

    @Override
    public List<UserCouponDTO> getUserCouponList(User user) throws Exception {

        List<UserCoupon> userCouponList = userCouponRepository.findAllByUserId(new ObjectId(user.getId()));
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();

        for(UserCoupon uc: userCouponList){
            UserCouponDTO userCouponDTO = new UserCouponDTO(uc);
            Coupon coupon = couponRepository.findById(userCouponDTO.getCouponId().toString()).orElseThrow(()->new Exception("존재하지 않는 쿠폰입니다."));
            userCouponDTO.setCoupon(new CouponDTO(coupon));
            userCouponDTOList.add(userCouponDTO);
        }

        return userCouponDTOList;
    }
}
