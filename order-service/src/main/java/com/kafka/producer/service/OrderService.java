package com.kafka.producer.service;

import com.kafka.producer.entity.Order;

public interface OrderService {

    Order createOrder(Order order);
    Order findById(String id);

    String deleteOrder(String id);

}
