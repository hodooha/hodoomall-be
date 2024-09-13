package com.hodoo.hodoomall.cart.service;

import com.hodoo.hodoomall.cart.model.dao.CartRepository;
import com.hodoo.hodoomall.cart.model.dto.Cart;
import com.hodoo.hodoomall.cart.model.dto.CartDTO;
import com.hodoo.hodoomall.product.model.dao.ProductRepository;
import com.hodoo.hodoomall.product.model.dto.Product;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CartDTO.CartItemDTO> getCartItems(User user) throws Exception {

        Cart cart;
        ObjectId userId = new ObjectId(user.getId());
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        List<CartDTO.CartItemDTO> cartItemDTOs = new ArrayList<>();

        if (existingCart.isPresent()) {
            cart = existingCart.get();
        } else {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }

        if (cart.getItems() != null && !cart.getItems().isEmpty()) {

            for(Cart.CartItem item : cart.getItems()){
                CartDTO.CartItemDTO cartItemDTO = new CartDTO.CartItemDTO();

                Product product = productRepository.findById(String.valueOf(item.getProductId())).orElseThrow(() -> new Exception("해당 상품이 존재하지 않습니다."));

                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(product.getId());
                productDTO.setCategory(product.getCategory());
                productDTO.setSku(product.getSku());
                productDTO.setName(product.getName());
                productDTO.setPrice(product.getPrice());
                productDTO.setImage(product.getImage());
                productDTO.setDescription(product.getDescription());
                productDTO.setStatus(product.getStatus());
                productDTO.setStock(product.getStock());

                cartItemDTO.setProductId(productDTO);
                cartItemDTO.setSize(item.getSize());
                cartItemDTO.setQty(item.getQty());

                cartItemDTOs.add(cartItemDTO);
            }


        }

        return cartItemDTOs;
    }

    @Override
    public int getCartQty(User user) throws Exception {

        Cart cart;
        ObjectId userId = new ObjectId(user.getId());
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        int cartQty = 0;

        if(existingCart.isPresent()){
            cart = existingCart.get();
            cartQty = cart.getItems().size();
        }

        return cartQty;
    }
}
