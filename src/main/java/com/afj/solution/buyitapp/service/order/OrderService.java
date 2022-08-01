package com.afj.solution.buyitapp.service.order;

import java.util.UUID;

import com.afj.solution.buyitapp.payload.request.CreateOrderRequest;
import com.afj.solution.buyitapp.payload.response.OrderResponse;

/**
 * @author Tomash Gombosh
 */
public interface OrderService {

    OrderResponse create(CreateOrderRequest createOrderRequest, UUID userId);

    void cancelOrder(UUID orderId);
}
