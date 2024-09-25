package com.hodoo.hodoomall.cart.controller;

import com.hodoo.hodoomall.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cart")
public class AdminCartController {

    private final CartService cartService;

}
