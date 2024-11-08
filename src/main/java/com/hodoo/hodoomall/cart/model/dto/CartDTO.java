package com.hodoo.hodoomall.cart.model.dto;

import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CartDTO {

    private String id;
    private String userId;
    private List<CartItemDTO> items;

    public Cart toEntity(){
        Cart cart = new Cart();
        cart.setId(this.id);
        cart.setUserId(new ObjectId(this.userId));
        cart.setItems(this.itemsToEntity());
        return cart;
    }

    public List<Cart.CartItem> itemsToEntity(){
        List<Cart.CartItem> itemsEntity = new ArrayList<>();
        for(CartItemDTO i : this.items){
            itemsEntity.add(i.toEntity());
        }
        return itemsEntity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemDTO {

        private ProductDTO product;
        private String size;
        private int qty = 1;
        private String productId;

        public CartItemDTO(ProductDTO productDTO, String size, int qty){
            this.product = productDTO;
            this.size = size;
            this.qty = qty;
            this.productId = productDTO.getId();
        }

        public Cart.CartItem toEntity() {
            return new Cart.CartItem(new ObjectId(productId), this.size, this.qty);
        }
    }

}
