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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CartDTO.CartItemDTO> getCartItems(User user) throws Exception {

        List<CartDTO.CartItemDTO> cartItemDTOs = new ArrayList<>();
        ObjectId userId = new ObjectId(user.getId());
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(new Cart(userId, new ArrayList<>())));

        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            for (Cart.CartItem item : cart.getItems()) {
                Product product = productRepository.findById(item.getProductId().toString()).orElseThrow(() -> new Exception("해당 상품이 존재하지 않습니다."));
                CartDTO.CartItemDTO cartItemDTO = new CartDTO.CartItemDTO(new ProductDTO(product), item.getSize(), item.getQty());
                cartItemDTOs.add(cartItemDTO);
            }
        }
        return cartItemDTOs;
    }

    @Override
    public int getCartQty(User user) throws Exception {
        ObjectId userId = new ObjectId(user.getId());

        return cartRepository.findByUserId(userId).map(cart -> cart.getItems().size()).orElse(0);
    }

    @Override
    public void addItemToCart(User user, CartDTO.CartItemDTO cartItemDTO) throws Exception {

        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));
        ObjectId userId = new ObjectId(user.getId());
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(new Cart(userId, new ArrayList<>())));

        boolean itemExists = false;
        for (Cart.CartItem i : cart.getItems()) {
            if (i.getProductId().equals(new ObjectId(cartItemDTO.getProductId())) && i.getSize().equals(cartItemDTO.getSize())) {
                i.setQty(i.getQty() + cartItemDTO.getQty());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cart.getItems().add(cartItemDTO.toEntity());
        }

        cartRepository.save(cart);
    }

    @Override
    public void updateQty(User user, CartDTO.CartItemDTO cartItemDTO) throws Exception {

        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));

        ObjectId userId = new ObjectId(user.getId());
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(new Cart(userId, new ArrayList<>())));

        for (Cart.CartItem i : cart.getItems()) {
            if (i.getProductId().equals(new ObjectId(cartItemDTO.getProductId())) && i.getSize().equals(cartItemDTO.getSize())) {
                i.setQty(cartItemDTO.getQty());
            }
            break;
        }

        cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(User user, CartDTO.CartItemDTO cartItemDTO) throws Exception {

        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new Exception("상품이 존재하지 않습니다."));

        ObjectId userId = new ObjectId(user.getId());
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new Exception("카트가 존재하지 않습니다."));

        cart.getItems().removeIf(item -> item.getProductId().equals(new ObjectId(cartItemDTO.getProductId())) && item.getSize().equals(cartItemDTO.getSize()));

        cartRepository.save(cart);
    }

    @Override
    public void emptyCartItem(User user) throws Exception {

        Cart cart = cartRepository.findByUserId(new ObjectId(user.getId())).orElseThrow(() -> new Exception("카트가 존재하지 않습니다."));

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
