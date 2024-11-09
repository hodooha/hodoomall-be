package com.hodoo.hodoomall.userCoupon.service;

import com.hodoo.hodoomall.coupon.model.dao.CouponRepository;
import com.hodoo.hodoomall.coupon.model.dto.Coupon;
import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.service.CouponService;
import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dao.UserCouponRepository;
import com.hodoo.hodoomall.userCoupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCoupon;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponRequestDTO;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.coupon.routing.key}")
    private String couponRoutingKey;


    @Override
    public void verifyUserCoupon(OrderDTO data) throws Exception {
        if (couponService.getCouponDetail(data.getUserCouponId()).getMinCost() > data.getTotalPrice())
            throw new Exception("쿠폰 사용 조건을 충족하지 않습니다.");
    }

    @Override
    public void checkUserCoupon(UserCouponDTO userCouponDTO) throws Exception {
        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty()) {
            throw new Exception("이미 받으신 쿠폰입니다.");
        }

        couponService.minusCouponQty(userCouponDTO.getCouponId());
        rabbitTemplate.convertAndSend(exchangeName, couponRoutingKey, new UserCouponRequestDTO(userCouponDTO.getCouponId().toString(), userCouponDTO.getUserId().toString(), userCouponDTO.getDuration()));
    }

    @RabbitListener(queues = "#{@couponQueue}", concurrency = "10-20")
    public void manageUserCouponRequest(UserCouponRequestDTO userCouponRequestDTO) throws Exception {

        UserCouponDTO userCouponDTO = new UserCouponDTO();
        userCouponDTO.setCouponId(new ObjectId(userCouponRequestDTO.getCouponId()));
        userCouponDTO.setUserId(new ObjectId(userCouponRequestDTO.getUserId()));
        userCouponDTO.setDuration(userCouponRequestDTO.getDuration());
        createUserCoupon(userCouponDTO);

    }

    @Override
    public void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception {

        UserCoupon userCoupon = userCouponDTO.toEntity();
        userCouponRepository.save(userCoupon);
    }

    @Override
    public List<UserCouponDTO> getUserCouponList(User user) throws Exception {

        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setUserId(new ObjectId(user.getId()));
        queryDTO.setUsed(false);
        queryDTO.setExpired(false);
        List<UserCoupon> userCouponList = userCouponRepository.findByQuery(queryDTO);
        System.out.println(userCouponList);
        List<UserCouponDTO> userCouponDTOList = new ArrayList<>();

        if (userCouponList != null && !userCouponList.isEmpty()) {
            List<String> couponIdList = userCouponList.stream().map((userCoupon) -> userCoupon.getCouponId().toString()).toList();
            Map<String, Coupon> couponMap = couponRepository.findAllById(couponIdList).stream().collect(Collectors.toMap(Coupon::getId, coupon -> coupon));


            for (UserCoupon uc : userCouponList) {
                UserCouponDTO userCouponDTO = new UserCouponDTO(uc);
                Coupon coupon = couponMap.get(uc.getCouponId().toString());
                userCouponDTO.setCoupon(new CouponDTO(coupon));
                userCouponDTOList.add(userCouponDTO);
            }
        }

        return userCouponDTOList;
    }

    @Override
    public void useUserCoupon(String userCouponId) throws Exception {

        UserCoupon usedCoupon = userCouponRepository.useUserCoupon(userCouponId);
        if (usedCoupon == null) throw new Exception("쿠폰이 존재하지 않거나 사용 조건을 충족하지 않습니다.");
    }


}
