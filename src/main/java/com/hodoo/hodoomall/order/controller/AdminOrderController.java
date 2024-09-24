package com.hodoo.hodoomall.order.controller;

import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import com.hodoo.hodoomall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

        @GetMapping()
        public ResponseEntity<?> getAllOrders(@ModelAttribute QueryDTO queryDTO) {

            try {
                List<OrderDTO> orderList = orderService.getOrder(queryDTO);
                long totalOrders = orderService.getTotalOrderCount(queryDTO);
                int totalPageNum = (int) Math.ceil((double) totalOrders/queryDTO.getPageSize());

                System.out.println(orderList);
                return ResponseEntity.ok().body(Map.of("status", "success", "orderList", orderList, "totalPageNum", totalPageNum));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(Map.of("status", "fal", "error", e.getMessage()));
            }


        }

    @PutMapping()
    public ResponseEntity<?> updateOrder(@RequestBody QueryDTO queryDTO){

        System.out.println(queryDTO);
        try{
            orderService.updateOrder(queryDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));

        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }
}
