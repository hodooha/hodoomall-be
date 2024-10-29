package com.hodoo.hodoomall.order.controller;

import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import com.hodoo.hodoomall.order.service.OrderService;
import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderDTO data) {

        System.out.println(data);

        try {
            User user = customUserDetails.getUser();
            Order completedOrder = orderService.createOrder(user, data);
            String orderNum = completedOrder.getOrderNum();
            return ResponseEntity.ok().body(Map.of("status", "success", "orderNum", orderNum));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @GetMapping()
    public ResponseEntity<?> getOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @ModelAttribute QueryDTO queryDTO) {

        try {
            User user = customUserDetails.getUser();
            queryDTO.setUser(user);
            List<OrderDTO> orderList = orderService.getOrder(queryDTO);
            long totalOrders = orderService.getTotalOrderCount(queryDTO);
            int totalPageNum = (int) Math.ceil((double) totalOrders / queryDTO.getPageSize());

            System.out.println(orderList);
            return ResponseEntity.ok().body(Map.of("status", "success", "orderList", orderList, "totalPageNum", totalPageNum));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") String id) {

        try {
            QueryDTO queryDTO = new QueryDTO();
            queryDTO.setId(id);
            queryDTO.setUser(customUserDetails.getUser());
            System.out.println(queryDTO);
            orderService.cancelOrder(queryDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }


}
