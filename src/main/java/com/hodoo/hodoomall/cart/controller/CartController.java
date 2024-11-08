package com.hodoo.hodoomall.cart.controller;

import com.hodoo.hodoomall.cart.model.dto.CartDTO;
import com.hodoo.hodoomall.cart.service.CartService;
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
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping()
    public ResponseEntity<?> getCarItems(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        try {
            User user = customUserDetails.getUser();
            List<CartDTO.CartItemDTO> cartList = cartService.getCartItems(user);
            int cartItemCount = cartList.size();
            int totalPrice = cartList.stream().mapToInt(item ->
                    item.getQty() * item.getProduct().getPrice()).sum();

            return ResponseEntity.ok().body(Map.of("status", "success", "cartList", cartList, "cartItemCount", cartItemCount, "totalPrice", totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @GetMapping("/qty")
    public ResponseEntity<?> getCartQty(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        try {
            User user = customUserDetails.getUser();
            int cartItemCount = cartService.getCartQty(user);

            return ResponseEntity.ok().body(Map.of("status", "success", "cartItemCount", cartItemCount));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @PostMapping()
    public ResponseEntity<?> addItemToCart(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CartDTO.CartItemDTO cartItemDTO) {

        try {
            User user = customUserDetails.getUser();
            cartService.addItemToCart(user, cartItemDTO);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateQty(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CartDTO.CartItemDTO cartItemDTO) {

        try {
            User user = customUserDetails.getUser();
            cartService.updateQty(user, cartItemDTO);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") String id, @RequestParam("size") String size, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        try {
            User user = customUserDetails.getUser();
            CartDTO.CartItemDTO cartItemDTO = new CartDTO.CartItemDTO();
            cartItemDTO.setProductId(id);
            cartItemDTO.setSize(size);
            cartService.deleteCartItem(user, cartItemDTO);

            return ResponseEntity.ok().body(Map.of("status", "success"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }


}
