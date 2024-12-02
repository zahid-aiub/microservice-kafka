package com.kafka.producer.service.impl;

import com.kafka.producer.entity.Order;
import com.kafka.producer.repository.OrderRepository;
import com.kafka.producer.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @CachePut(value = "order", key = "#result.id", unless = "#result == null")
    public Order createOrder(Order order) {
        Order savedOrder = this.orderRepository.save(order);
        logger.info("Order created with ID: {}", savedOrder.getId());

        //Todo: better solution possible
        this.refreshCache("order");

        return savedOrder;
    }

    @Override
    @Cacheable(value = "order", unless = "#result == null")
    public Iterable<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    @Cacheable(value = "order", key = "#id", unless = "#result == null")
    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Caching(
            evict = {@CacheEvict(value = "order", allEntries = true)},
            put = {@CachePut(value = "order", key = "#id")}
    )
    public Order updateOrder(Long id, Order order) {
        Order savedOrder = this.orderRepository.findById(id).orElse(null);
        if (savedOrder == null) {
            return null;
        }
        savedOrder.setQuantity(order.getQuantity());
        savedOrder.setPrice(order.getPrice());

        return this.orderRepository.save(savedOrder);
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "order", allEntries = true),
                    @CacheEvict(value = "order", key = "#id")
            }
    )
    public String deleteOrder(Long id) {
        this.orderRepository.deleteById(id);

        return "Order deleted!";
    }

    public void refreshCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
