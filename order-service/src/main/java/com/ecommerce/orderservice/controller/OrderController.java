package com.ecommerce.orderservice.controller;


import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
       return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequestDTO));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"OOPS!!! Something went Wrong!!!!");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getALlOrders(){
        return orderService.getAllOrders();
    }
}
