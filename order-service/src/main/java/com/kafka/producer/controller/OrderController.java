package com.kafka.producer.controller;

import com.core_domain.dto.OrderEvent;
import com.kafka.producer.entity.Order;
import com.kafka.producer.orderproducer.OrderProducer;
import com.kafka.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public String createOrder(@RequestBody com.core_domain.dto.Order order) {

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

    @PostMapping("/add")
    public String addOrder(@RequestBody Order order) {

        Order order1 = this.orderService.createOrder(order);
        return "Order placed successfully!";
    }

    @GetMapping("/all")
    Iterable<Order> getOrders() {
        return this.orderService.getOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable() Long id) {
        return this.orderService.findById(id);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable() Long id, @RequestBody Order order) {
        return this.orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable() Long id) {

        return this.orderService.deleteOrder(id);
    }
}
