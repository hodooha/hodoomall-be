package com.hodoo.hodoomall.order.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hodoo.hodoomall.product.model.dto.ProductDTO;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {

    private String id;

    private String userCouponId;

    private String userId;
    private UserDTO user;

    private String status;
    private int totalPrice;

    private ShipToDTO shipTo;
    private ContactDTO contact;
    private String orderNum;

    private List<OrderItemDTO> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDTO(Order order){
        this.id = order.getId();
        this.userId = order.getUserId().toString();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.shipTo = new ShipToDTO(order.getShipTo());
        this.contact = new ContactDTO(order.getContact());
        this.orderNum = order.getOrderNum();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();

        List<OrderItemDTO> items = new ArrayList<>();
        for(Order.OrderItem i : order.getItems()){
            items.add(new OrderItemDTO(i));
        }

        this.items = items;

    }

    public List<Order.OrderItem> itemsToEntity(){
        List<Order.OrderItem> itemsEntity = new ArrayList<>();
        for(OrderItemDTO i : this.items){
            itemsEntity.add(i.toEntity());
        }
        return itemsEntity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShipToDTO{
        private String address;
        private String city;
        private String zip;

        public ShipToDTO(Order.ShipTo shipTo){
            this.address = shipTo.getAddress();
            this.city = shipTo.getCity();
            this.zip = shipTo.getZip();
        }

        public Order.ShipTo toEntity(){
            return new Order.ShipTo(this.address, this.city, this.zip);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactDTO{
        private String firstName;
        private String lastName;
        private String contact;

        public ContactDTO(Order.Contact contact){
            this.firstName = contact.getFirstName();
            this.lastName = contact.getLastName();
            this.contact = contact.getContact();
        }

        public Order.Contact toEntity(){
            return new Order.Contact(this.firstName, this.lastName, this.contact);
        }
    }

    @Data
    @NoArgsConstructor
    public static class OrderItemDTO{

        @Id
        private String id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ObjectId productId;

        private ProductDTO product;
        private int price;
        private int qty;
        private String size;

        public OrderItemDTO(Order.OrderItem orderItem){
            this.id = orderItem.getId();
            this.qty = orderItem.getQty();
            this.price = orderItem.getPrice();
            this.size = orderItem.getSize();
            this.productId = orderItem.getProductId();
        }

        public Order.OrderItem toEntity(){
            return new Order.OrderItem(this.productId, this.price, this.qty, this.size);
        }

    }
}
