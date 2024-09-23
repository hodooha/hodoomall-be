package com.hodoo.hodoomall.coupon.service;

import com.hodoo.hodoomall.coupon.model.dao.CouponRepository;
import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    @Override
    public void createCoupon(CouponDTO couponDTO) throws Exception {

        if(couponDTO.getDcRate() < 0 || couponDTO.getDcRate() > 100) throw new Exception("할인율은 0과 100 사이의 값이어야 합니다.");
        if(couponDTO.getDuration() < 0) throw new Exception("유효 기간은 1일 이상이어야 합니다.");
        Coupon coupon = couponDTO.toEntity();
        couponRepository.save(coupon);

    }

    @Override
    public List<CouponDTO> getCouponList(QueryDTO queryDTO) throws Exception {

        List<Coupon> couponList = couponRepository.findByQuery(queryDTO);
        List<CouponDTO> couponDTOList = new ArrayList<>();

        for(Coupon c : couponList){
            CouponDTO couponDTO = new CouponDTO(c);
            couponDTOList.add(couponDTO);
        }

        return couponDTOList;
    }

    @Override
    public long getTotalCouponCount(QueryDTO queryDTO) throws Exception {

        return couponRepository.getTotalCouponCount(queryDTO);
    }

    @Override
    public void deleteCoupon(String id) throws Exception {

        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));

        coupon.setDeleted(true);
        couponRepository.save(coupon);

    }

    @Override
    public CouponDTO getCouponDetail(String id) throws Exception {

        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));

        return new CouponDTO(coupon);
    }


}
