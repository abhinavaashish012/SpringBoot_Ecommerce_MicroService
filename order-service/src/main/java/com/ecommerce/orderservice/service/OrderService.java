package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderLineItemsDTO;
import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderLineItems;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        System.out.println("ORDER REQUEST : "+ orderRequestDTO);
        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItemsDTOList()
                                               .stream()
                                               .map(this::mapToDTO)
                                               .toList();

        order.setOrderLineItemsList(orderLineItems);

        System.out.println("ORDERLINE ITEMS :"+order.getOrderLineItemsList());
        orderRepository.save(order);
    }


    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
