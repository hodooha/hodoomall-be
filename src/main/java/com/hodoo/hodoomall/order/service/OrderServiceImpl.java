package com.hodoo.hodoomall.order.service;

import com.hodoo.hodoomall.order.model.dao.OrderRepository;
import com.hodoo.hodoomall.order.model.dto.Order;
import com.hodoo.hodoomall.order.model.dto.OrderDTO;
import com.hodoo.hodoomall.order.util.RandomString;
import com.hodoo.hodoomall.user.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;


    @Override
    public void createOrder(User user, OrderDTO data) throws Exception {

        Order order = new Order();

        order.setContact(data.getContact().toEntity());
        order.setShipTo(data.getShipTo().toEntity());
        order.setItems(data.itemsToEntity());
        order.setTotalPrice(data.getTotalPrice());
        order.setUserId(new ObjectId(user.getId()));
        order.setOrderNum(RandomString.generateRandomMixStr(11, false));

        orderRepository.save(order);

    }
}
