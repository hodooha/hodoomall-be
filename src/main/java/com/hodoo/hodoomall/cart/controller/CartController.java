package com.hodoo.hodoomall.cart.controller;

import com.hodoo.hodoomall.cart.model.dto.Cart;
import com.hodoo.hodoomall.cart.model.dto.CartDTO;
import com.hodoo.hodoomall.cart.service.CartService;
import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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

        User user = customUserDetails.getUser();
        try {
            List<CartDTO.CartItemDTO> cartList = cartService.getCartItems(user);
            int cartItemCount = cartList.size();
            int totalPrice = cartList.stream().mapToInt(item ->
                    item.getQty() * item.getProductId().getPrice()
            ).sum();


            return ResponseEntity.ok().body(Map.of("status", "success", "cartList", cartList, "cartItemCount", cartItemCount, "totalPrice", totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @GetMapping("/qty")
    public ResponseEntity<?> getCartQty(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        User user = customUserDetails.getUser();

        try {
            int cartItemCount = cartService.getCartQty(user);
            return ResponseEntity.ok().body(Map.of("status", "success", "cartItemCount",cartItemCount));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @PostMapping()
    public ResponseEntity<?> addItemToCart(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody Map<String, String> item){

        System.out.println(item);

        try {
            User user = customUserDetails.getUser();
            Cart.CartItem cartItem = new Cart.CartItem();
            cartItem.setProductId(new ObjectId(item.get("id")));
            cartItem.setSize(item.get("size"));

            cartService.addItemToCart(user, cartItem);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateQty(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody Map<String, String> item){

        User user = customUserDetails.getUser();
        Cart.CartItem cartItem = new Cart.CartItem();
        cartItem.setProductId(new ObjectId(item.get("id")));
        cartItem.setSize(item.get("size"));
        cartItem.setQty(Integer.parseInt(item.get("qty")));
        try {
            cartService.updateQty(user, cartItem);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }


}
