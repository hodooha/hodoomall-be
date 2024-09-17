package com.hodoo.hodoomall.order.controller;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.service.OrderService;
import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderDTO data){

        System.out.println(data);

        try {
            User user = customUserDetails.getUser();
            orderService.createOrder(user, data);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

}
