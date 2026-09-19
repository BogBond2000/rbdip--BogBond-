package com.rbdip.bookstore.order;

import com.rbdip.bookstore.purchase.PurchaseVerificationPort;
import org.springframework.stereotype.Service;

@Service
public class OrderPurchaseVerificationAdapter implements PurchaseVerificationPort {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderPurchaseVerificationAdapter(
            OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public boolean hasAnyPurchase() {
        return orderRepository.count() > 0 && orderItemRepository.count() > 0;
    }
}
