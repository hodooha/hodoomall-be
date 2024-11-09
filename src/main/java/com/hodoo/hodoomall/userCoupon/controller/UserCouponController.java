package com.hodoo.hodoomall.userCoupon.controller;

import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.userCoupon.model.dto.UserCouponDTO;
import com.hodoo.hodoomall.userCoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userCoupon")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping()
    public ResponseEntity<?> createUserCoupon(@RequestBody UserCouponDTO userCouponDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        try {
            User user = customUserDetails.getUser();
            userCouponDTO.setUserId(new ObjectId(user.getId()));
            userCouponService.checkUserCoupon(userCouponDTO);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<?> getUserCouponList(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        try {
            User user = customUserDetails.getUser();
            List<UserCouponDTO> userCouponList = userCouponService.getUserCouponList(user);
            return ResponseEntity.ok().body(Map.of("status", "success", "userCouponList", userCouponList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }

}
