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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
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

            for (Cart.CartItem item : cart.getItems()) {

                Product product = productRepository.findById(String.valueOf(item.getProductId())).orElseThrow(() -> new Exception("해당 상품이 존재하지 않습니다."));

                ProductDTO productDTO = new ProductDTO(product);

                CartDTO.CartItemDTO cartItemDTO = new CartDTO.CartItemDTO(productDTO, item.getSize(), item.getQty());

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

        if (existingCart.isPresent()) {
            cart = existingCart.get();
            cartQty = cart.getItems().size();
        }

        return cartQty;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addItemToCart(User user, Cart.CartItem cartItem) throws Exception {

        Cart cart;

        Optional<Product> product = productRepository.findById(cartItem.getProductId().toString());
        if (product.isEmpty()) throw new Exception("상품이 존재하지 않습니다.");

        ObjectId userId = new ObjectId(user.getId());
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            cart = existingCart.get();
        } else {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }


        for (Cart.CartItem i : cart.getItems()) {
            if (i.getProductId().equals(cartItem.getProductId()) && i.getSize().equals(cartItem.getSize())) {
                throw new Exception("이미 추가된 아이템입니다.");
            }
        }

        cart.getItems().add(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void updateQty(User user, Cart.CartItem cartItem) throws Exception {

        Cart cart;

        Optional<Product> product = productRepository.findById(cartItem.getProductId().toString());
        if (product.isEmpty()) throw new Exception("상품이 존재하지 않습니다.");

        ObjectId userId = new ObjectId(user.getId());
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isEmpty()) throw new Exception("카트가 존재하지 않습니다.");

        cart = existingCart.get();

        for (Cart.CartItem i : cart.getItems()) {
            if (i.getProductId().equals(cartItem.getProductId()) && i.getSize().equals(cartItem.getSize())) {
                i.setQty(cartItem.getQty());
            }
        }

        cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(User user, String id) throws Exception {

        Cart cart;

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) throw new Exception("상품이 존재하지 않습니다.");

        ObjectId userId = new ObjectId(user.getId());
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isEmpty()) throw new Exception("카트가 존재하지 않습니다.");

        cart = existingCart.get();

        for (Cart.CartItem i : cart.getItems()) {
            if (i.getProductId().equals(new ObjectId(id))) {
                cart.getItems().remove(i);
                break;
            }
        }

        cartRepository.save(cart);

    }

    @Override
    public void emptyCartItem(User user) throws Exception {

        Cart cart = cartRepository.findByUserId(new ObjectId(user.getId())).orElseThrow(() -> new Exception("카트가 존재하지 않습니다."));

        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);
    }
}
