package com.rbdip.bookstore.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.rbdip.bookstore.product.Product;
import com.rbdip.bookstore.product.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderLineItemResolverTest {

    @Mock
    private ProductRepository productRepository;

    private OrderLineItemResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new OrderLineItemResolver(productRepository);
    }

    @Test
    void resolvesProductsAndLineItems() {
        Product product = new Product("Clean Code", new BigDecimal("35.00"), null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        CreateOrderRequest request = new CreateOrderRequest(
                "Ivan Petrov", "Moscow", null, "regular", null,
                List.of(new CreateOrderRequest.Item(1L, 2)));

        OrderLineItemResolver.ResolvedLineItems resolved = resolver.resolve(request);

        assertThat(resolved.products()).containsExactly(product);
        assertThat(resolved.lineItems()).containsExactly(
                new PricingCalculator.LineItem(new BigDecimal("35.00"), 2));
    }

    @Test
    void defaultsNullQuantityToOne() {
        Product product = new Product("Refactoring", new BigDecimal("40.00"), null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        CreateOrderRequest request = new CreateOrderRequest(
                "Anna", "SPb", null, "regular", null,
                List.of(new CreateOrderRequest.Item(1L, null)));

        OrderLineItemResolver.ResolvedLineItems resolved = resolver.resolve(request);

        assertThat(resolved.lineItems()).containsExactly(
                new PricingCalculator.LineItem(new BigDecimal("40.00"), 1));
    }

    @Test
    void rejectsMissingProduct() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                "Anna", "SPb", null, "regular", null,
                List.of(new CreateOrderRequest.Item(99L, 1)));

        assertThatThrownBy(() -> resolver.resolve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("product 99 not found");
    }
}
