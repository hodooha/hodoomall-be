package com.hodoo.hodoomall.coupon.controller;

import com.hodoo.hodoomall.coupon.model.dto.CouponDTO;
import com.hodoo.hodoomall.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {

    private final CouponService couponService;

    @PostMapping()
    public ResponseEntity<?> createCoupon(@RequestBody CouponDTO couponDTO){

        System.out.println(couponDTO);

        try {
            couponService.createCoupon(couponDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "error", "error", e.getMessage()));
        }

    }

    @PutMapping()
    public ResponseEntity<?> editCoupon(@RequestBody CouponDTO couponDTO){

        try{
            couponService.editCoupon(couponDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "error", "error", e.getMessage()));
        }


    }



    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("id") String id){

        System.out.println(id);

        try {
            couponService.deleteCoupon(id);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "success", "error", e.getMessage()));
        }

    }

}
