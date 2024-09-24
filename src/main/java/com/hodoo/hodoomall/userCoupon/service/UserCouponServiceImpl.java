package com.hodoo.hodoomall.userCoupon.service;

import com.hodoo.hodoomall.coupon.model.dao.CouponRepository;
import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.service.CouponService;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dao.UserCouponRepository;
import com.hodoo.hodoomall.userCoupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @Override
    public void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception {

        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty())
            throw new Exception("이미 받으신 쿠폰입니다.");

        if (!couponService.checkCouponQty(userCouponDTO.getCouponId())) throw new Exception("쿠폰이 모두 소진되었습니다.");

        UserCoupon userCoupon = userCouponDTO.toEntity();
        userCouponRepository.save(userCoupon);

        couponService.minusCouponQty(userCouponDTO.getCouponId());

    }

    @Override
    public List<UserCouponDTO> getUserCouponList(User user) throws Exception {

        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setUserId(new ObjectId(user.getId()));
        queryDTO.setUsed(false);
        queryDTO.setExpired(false);
        List<UserCoupon> userCouponList = userCouponRepository.findByQuery(queryDTO);
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();

        for (UserCoupon uc : userCouponList) {
            UserCouponDTO userCouponDTO = new UserCouponDTO(uc);
            Coupon coupon = couponRepository.findById(userCouponDTO.getCouponId().toString()).orElseThrow(() -> new Exception("존재하지 않는 쿠폰입니다."));
            userCouponDTO.setCoupon(new CouponDTO(coupon));
            userCouponDTOList.add(userCouponDTO);
        }

        return userCouponDTOList;
    }

    @Override
    public void useUserCoupon(String userCouponId) throws Exception {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));

        if (userCoupon.getExpiredAt().isBefore(LocalDate.now())) throw new Exception("기간 만료된 쿠폰입니다.");

        userCoupon.setUsed(true);
        userCoupon.setUsedAt(LocalDateTime.now());
        userCouponRepository.save(userCoupon);

    }
}
