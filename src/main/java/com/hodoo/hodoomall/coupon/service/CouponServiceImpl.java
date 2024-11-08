package com.hodoo.hodoomall.coupon.service;

import com.hodoo.hodoomall.coupon.model.dao.CouponRepository;
import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dao.UserCouponRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisTemplate<String,Integer> redisTemplate;


    static final String DC_RATE = "dcRate";
    static final String DC_PRICE = "dcPrice";


    @PostConstruct
    public void initializeCache(){
        initializeCouponCache();
    }

    public void initializeCouponCache(){
        List<Coupon> coupons = couponRepository.findAll();

        for (Coupon coupon : coupons) {
            String redisKey = "coupon:" + coupon.getId() + ":quantity";
            redisTemplate.opsForValue().set(redisKey, coupon.getTotalQty());
        }
    }

    @Override
    public void minusCouponQty(ObjectId couponId) throws Exception {

        String redisKey = "coupon:" + couponId.toString() + ":quantity";
        Integer cachedQty = redisTemplate.opsForValue().get(redisKey);

        if (cachedQty != null && cachedQty <= 0) {
            throw new Exception("쿠폰이 모두 소진되었습니다.");
        }

        Long remainingQty = redisTemplate.opsForValue().decrement(redisKey);

        if (remainingQty == null || remainingQty < 0) {
            redisTemplate.opsForValue().increment(redisKey);
            throw new Exception("쿠폰이 모두 소진되었습니다.");
        }

    }

    @Scheduled(fixedRate = 30000)
    public void syncCouponQuantity() {

        List<Coupon> coupons = couponRepository.findAll();

        for (Coupon coupon : coupons) {
            String redisKey = "coupon:" + coupon.getId() + ":quantity";
            Integer cachedQty = (Integer) redisTemplate.opsForValue().get(redisKey);

            if (cachedQty != null) {
                coupon.setTotalQty(cachedQty);
                couponRepository.save(coupon);
            }
        }
    }

    @Override
    public void createCoupon(CouponDTO couponDTO) throws Exception {

        if (couponDTO.getType().equals(DC_RATE) && (couponDTO.getDcAmount() < 0 || couponDTO.getDcAmount() > 100))
            throw new Exception("할인율은 0과 100 사이의 값이어야 합니다.");
        if (couponDTO.getType().equals(DC_PRICE) && couponDTO.getDcAmount() < 0)
            throw new Exception("할인금액은 0원보다 커야합니다.");

        if (couponDTO.getDuration() < 0) throw new Exception("유효 기간은 1일 이상이어야 합니다.");
        Coupon coupon = couponRepository.save(couponDTO.toEntity());

        String redisKey = "coupon:"+coupon.getId()+":quantity";
        redisTemplate.opsForValue().set(redisKey, coupon.getTotalQty());

    }

    @Override
    public List<CouponDTO> getCouponList(QueryDTO queryDTO) throws Exception {
        List<Coupon> couponList = couponRepository.findByQuery(queryDTO);
        return couponList.stream().map(coupon -> new CouponDTO(coupon)).collect(Collectors.toList());
    }

    @Override
    public long getTotalCouponCount(QueryDTO queryDTO) throws Exception {
        return couponRepository.getTotalCouponCount(queryDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCoupon(String id) throws Exception {

        couponRepository.deleteById(id);
        userCouponRepository.deleteByCouponId(id);

        String redisKey = "coupon:" + id + ":quantity";
        redisTemplate.delete(redisKey);
    }

    @Override
    public CouponDTO getCouponDetail(String id) throws Exception {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));
        return new CouponDTO(coupon);
    }

    @Override
    public void editCoupon(CouponDTO couponDTO) throws Exception {

        Coupon coupon = couponRepository.findById(couponDTO.getId()).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));

        if (couponDTO.getType().equals(DC_RATE) && (couponDTO.getDcAmount() < 0 || couponDTO.getDcAmount() > 100))
            throw new Exception("할인율은 0과 100 사이의 값이어야 합니다.");
        if (couponDTO.getType().equals(DC_PRICE) && couponDTO.getDcAmount() < 0)
            throw new Exception("할인금액은 0원보다 커야합니다.");

        if (couponDTO.getDuration() < 0) throw new Exception("유효 기간은 1일 이상이어야 합니다.");

        Coupon updatedCoupon = couponDTO.toEntity();
        updatedCoupon.setId(couponDTO.getId());
        couponRepository.save(updatedCoupon);
    }


}
