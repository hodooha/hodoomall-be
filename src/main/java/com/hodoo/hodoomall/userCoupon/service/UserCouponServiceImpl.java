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
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponRequestDTO;
import com.mongodb.MongoCommandException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // 캐싱X, 메시지큐X
    @Retryable(
            // 트랜잭션 충돌 발생 시 재시도
            maxAttempts = 30, // 최대 3번 재시도
            backoff = @Backoff(delay = 150, multiplier = 1.1) // 150ms부터 시작, 1.1배씩 증가
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUserCoupon0(UserCouponDTO userCouponDTO) throws Exception {

        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty())
            throw new Exception("이미 다운받은 쿠폰입니다.");

        couponService.minusCouponQty0(userCouponDTO.getCouponId());

        UserCoupon userCoupon = userCouponDTO.toEntity();
        userCouponRepository.save(userCoupon);

    }

    // 캐싱O, 메시지큐X
    @Override
    @Retryable(
            // 트랜잭션 충돌 발생 시 재시도
            value = {MongoCommandException.class},
            maxAttempts = 20, // 최대 3번 재시도
            backoff = @Backoff(delay = 150, multiplier = 1.1) // 150ms부터 시작, 1.1배씩 증가
    )
    @Transactional(rollbackFor = Exception.class)
    public void createUserCoupon(UserCouponDTO userCouponDTO) throws Exception {

        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty())
            throw new Exception("이미 받으신 쿠폰입니다.");
        couponService.minusCouponQty(userCouponDTO.getCouponId());
        UserCoupon userCoupon = userCouponDTO.toEntity();
        userCouponRepository.save(userCoupon);
    }

    // 캐싱O, 메시지큐O
    @Override
    public void checkUserCoupon(UserCouponDTO userCouponDTO) throws Exception {
        if (!userCouponRepository.findAllByCouponIdAndUserId(userCouponDTO.getCouponId(), userCouponDTO.getUserId()).isEmpty()) {
            throw new Exception("이미 받으신 쿠폰입니다.");
        }

        couponService.minusCouponQty(userCouponDTO.getCouponId());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, new UserCouponRequestDTO(userCouponDTO.getCouponId().toString(), userCouponDTO.getUserId().toString(), userCouponDTO.getDuration()));
    }

    // 캐싱O, 메시지큐O
    @RabbitListener(queues = "${rabbitmq.queue.name}", concurrency = "10-20")
    public void manageUserCouponRequest(UserCouponRequestDTO userCouponRequestDTO) throws Exception {

        UserCouponDTO userCouponDTO = new UserCouponDTO();
        userCouponDTO.setCouponId(new ObjectId(userCouponRequestDTO.getCouponId()));
        userCouponDTO.setUserId(new ObjectId(userCouponRequestDTO.getUserId()));
        userCouponDTO.setDuration(userCouponRequestDTO.getDuration());
        createUserCoupon1(userCouponDTO);

    }

    // 캐싱O, 메시지큐O
    @Override
    public void createUserCoupon1(UserCouponDTO userCouponDTO) throws Exception {

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
    public void useUserCoupon(String userCouponId, int totalPrice) throws Exception {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(() -> new Exception("쿠폰이 존재하지 않습니다."));

        if (userCoupon.getExpiredAt().isBefore(LocalDate.now())) throw new Exception("기간 만료된 쿠폰입니다.");

        if (couponService.getCouponDetail(userCoupon.getCouponId().toString()).getMinCost() < totalPrice) {
            throw new Exception("최소 주문 금액 조건이 맞지 않습니다.");
        }
        userCoupon.setUsed(true);
        userCoupon.setUsedAt(LocalDateTime.now());
        userCouponRepository.save(userCoupon);

    }


}
