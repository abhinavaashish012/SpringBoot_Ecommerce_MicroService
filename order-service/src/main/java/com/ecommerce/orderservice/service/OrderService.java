package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.InventoryResponse;
import com.ecommerce.orderservice.dto.OrderLineItemsDTO;
import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderLineItems;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public void placeOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        //System.out.println("ORDER REQUEST : "+ orderRequestDTO);

        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItemsDTOList()
                                               .stream()
                                               .map(this::mapToDTO)
                                               .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodeList = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // call inventory service and place order if the item exists in stock
        InventoryResponse[] inventoryResponsesArray = webClient.get().uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();


        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                                .allMatch(InventoryResponse::isInStock);
        if (allProductsInStock)
        {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Product is not in stock...please try again...");
        }
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
