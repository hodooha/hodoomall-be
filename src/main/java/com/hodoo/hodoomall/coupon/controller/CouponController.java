package com.hodoo.hodoomall.coupon.controller;

import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.model.dto.QueryDTO;
import com.hodoo.hodoomall.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("{id}")
    public ResponseEntity<?> getCouponDetail(@PathVariable("id") String id){

        try {
            CouponDTO couponDTO = couponService.getCouponDetail(id);
            return ResponseEntity.ok().body(Map.of("status", "success", "selectedCoupon", couponDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "success", "error", e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<?> getCouponList(@ModelAttribute QueryDTO queryDTO){

        try {
            List<CouponDTO> couponList = couponService.getCouponList(queryDTO);
            long totalCoupons = couponService.getTotalCouponCount(queryDTO);
            int totalPageNum = (int) Math.ceil((double) totalCoupons/queryDTO.getPageSize());

            return ResponseEntity.ok().body(Map.of("status", "success", "couponList", couponList, "totalPageNum", totalPageNum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "success", "error", e.getMessage()));
        }

    }





}
