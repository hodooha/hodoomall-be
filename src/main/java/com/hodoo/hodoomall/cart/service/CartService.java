package com.hodoo.hodoomall.cart.service;

import com.hodoo.hodoomall.cart.model.dto.CartDTO;
import com.hodoo.hodoomall.user.model.dto.User;

import java.util.List;

public interface CartService {
    List<CartDTO.CartItemDTO> getCartItems(User user) throws Exception;

    int getCartQty(User user) throws Exception;
}
