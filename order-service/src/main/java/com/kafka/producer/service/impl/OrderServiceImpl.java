package com.kafka.producer.service.impl;

import com.kafka.producer.entity.Order;
import com.kafka.producer.repository.OrderRepository;
import com.kafka.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Cacheable(value = "order")
    public Order createOrder(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    @Cacheable(value = "order", key = "#id")
    public Order findById(String id) {
        return this.orderRepository.findById(id).orElseThrow();
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "order", allEntries = true),
                    @CacheEvict(value = "order", key = "#id")
            }
    )
    public String deleteOrder(String id) {
        this.orderRepository.deleteById(String.valueOf(id));

        return "Order deleted!";
    }

}
