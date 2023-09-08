package com.ecommerce.orderservice.controller;


import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        orderService.placeOrder(orderRequestDTO);
        return "Order Placed Successfully.";
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getALlOrders(){
        return orderService.getAllOrders();
    }
}
