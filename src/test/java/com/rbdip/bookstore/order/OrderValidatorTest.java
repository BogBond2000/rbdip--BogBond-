package com.rbdip.bookstore.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderValidatorTest {

    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }

    @Test
    void rejectsBlankCustomerName() {
        CreateOrderRequest request = new CreateOrderRequest(
                "  ", "Address", null, "regular", null,
                List.of(new CreateOrderRequest.Item(1L, 1)));

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("customerFullName");
    }

    @Test
    void rejectsBlankAddress() {
        CreateOrderRequest request = new CreateOrderRequest(
                "Name", "", null, "regular", null,
                List.of(new CreateOrderRequest.Item(1L, 1)));

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("customerAddress");
    }

    @Test
    void rejectsEmptyItems() {
        CreateOrderRequest request = new CreateOrderRequest(
                "Name", "Address", null, "regular", null, List.of());

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one item");
    }

    @Test
    void rejectsNonPositiveQuantity() {
        CreateOrderRequest request = new CreateOrderRequest(
                "Name", "Address", null, "regular", null,
                List.of(new CreateOrderRequest.Item(1L, 0)));

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity");
    }
}
