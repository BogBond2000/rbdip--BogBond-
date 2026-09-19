package com.rbdip.bookstore.order;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validate(CreateOrderRequest request) {
        requireNonBlank(request.customerFullName(), "customerFullName is required");
        requireNonBlank(request.customerAddress(), "customerAddress is required");
        requireNonEmptyItems(request.items());
        validateQuantities(request.items());
    }

    private void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireNonEmptyItems(List<CreateOrderRequest.Item> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
    }

    private void validateQuantities(List<CreateOrderRequest.Item> items) {
        for (CreateOrderRequest.Item item : items) {
            int quantity = item.quantity() == null ? 1 : item.quantity();
            if (quantity <= 0) {
                throw new IllegalArgumentException("quantity must be positive");
            }
        }
    }
}
