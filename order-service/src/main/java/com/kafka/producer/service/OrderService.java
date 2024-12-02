package com.kafka.producer.service;

import com.kafka.producer.entity.Order;

public interface OrderService {

    Order createOrder(Order order);

    Iterable<Order> getOrders();

    Order findById(Long id);

    Order updateOrder(Long id, Order order);

    String deleteOrder(Long id);

}
