package com.kafka.producer.controller;

import com.core_domain.dto.Order;
import com.core_domain.dto.OrderEvent;
import com.kafka.producer.orderproducer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderProducer orderProducer;

    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {

        order.setId(UUID.randomUUID().toString());
        OrderEvent orderEvent = new OrderEvent(
                UUID.randomUUID().toString(),
                "Order is pending",
                "PENDING",
                order
        );

        this.orderProducer.sendMessage(orderEvent);

        return "Order placed successfully!";
    }
}
