package com.nldinh.order_service.service;

import com.nldinh.order_service.dto.InventoryResponse;
import com.nldinh.order_service.dto.OrderLineItemsDto;
import com.nldinh.order_service.dto.OrderRequest;
import com.nldinh.order_service.model.Order;
import com.nldinh.order_service.model.OrderLineItems;
import com.nldinh.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map((OrderLineItemsDto) -> mapToDto(OrderLineItemsDto))
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);
        // Code 1
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(orderLineItems -> orderLineItems.getSkuCode())
                .toList();
        // Code 2
//        List<String> skuCodes = new ArrayList<>();
//        for(int i = 0; i < order.getOrderLineItemsList().size(); i++){
//            skuCodes.add(order.getOrderLineItemsList().get(i).getSkuCode());
//        }
//        Code 1 and Code 2 are the same, but code 1 is prefered cause it's functional programming

        // Call inventory service, and place order if product is in stock
        InventoryResponse[] InventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        // If all the element inside InventoryResponseArray is true then return true, if even once is false then return false
        boolean allProductsInStock = Arrays.stream(InventoryResponseArray)
                .allMatch(InventoryResponse -> InventoryResponse.isInStock());

        if(allProductsInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
