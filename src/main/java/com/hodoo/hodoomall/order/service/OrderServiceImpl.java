package com.hodoo.hodoomall.order.service;

import com.hodoo.hodoomall.cart.service.CartService;
import com.hodoo.hodoomall.order.model.dao.OrderRepository;
import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.model.dto.QueryDTO;
import com.hodoo.hodoomall.order.util.RandomString;
import com.hodoo.hodoomall.product.model.dto.StockCheckResultDTO;
import com.hodoo.hodoomall.product.service.ProductService;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.user.service.UserService;
import com.hodoo.hodoomall.userCoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;
    private final UserCouponService userCouponService;


    @Override
    public Order createOrder(User user, OrderDTO data) throws Exception {

        Order order = new Order();

        List<OrderDTO.OrderItemDTO> items = data.getItems();
        List<String> insufficientStockItems = new ArrayList<>();

        for (OrderDTO.OrderItemDTO i : items) {

            StockCheckResultDTO result = productService.checkStock(i);

            if (!result.isVerify()) {
                insufficientStockItems.add(result.getMessage());
            }

        }

        if (!insufficientStockItems.isEmpty()) throw new Exception(insufficientStockItems.toString());

        for (OrderDTO.OrderItemDTO i : items) {
            productService.updateStock(i);
        }

        if(data.getUserCouponId() != null) {
            userCouponService.useUserCoupon(data.getUserCouponId());
        }

        order.setContact(data.getContact().toEntity());
        order.setShipTo(data.getShipTo().toEntity());
        order.setItems(data.itemsToEntity());
        order.setTotalPrice(data.getTotalPrice());
        order.setUserId(new ObjectId(user.getId()));
        order.setOrderNum(RandomString.generateRandomMixStr(11, false));

        cartService.emptyCartItem(user);

        return orderRepository.save(order);

    }

    @Override
    public List<OrderDTO> getOrder(QueryDTO queryDTO) throws Exception {

        List<Order> orders = orderRepository.findByQuery(queryDTO);

        List<OrderDTO> orderList = new ArrayList<>();
        for (Order o : orders) {
            OrderDTO orderDTO = new OrderDTO(o);
            for (OrderDTO.OrderItemDTO i : orderDTO.getItems()) {
                i.setProduct(productService.getProductDetail(i.getProductId().toString()));
            }

            orderDTO.setUser(userService.findById(o.getUserId().toString()));
            orderList.add(orderDTO);
        }

        return orderList;
    }

    @Override
    public long getTotalOrderCount(QueryDTO queryDTO) throws Exception {
        return orderRepository.getTotalOrderCount(queryDTO);
    }

    @Override
    public void updateOrder(QueryDTO queryDTO) throws Exception {

        Order order = orderRepository.findById(queryDTO.getId()).orElseThrow(()-> new Exception("주문이 존재하지 않습니다."));

        order.setStatus(queryDTO.getStatus());
        orderRepository.save(order);

    }
}
