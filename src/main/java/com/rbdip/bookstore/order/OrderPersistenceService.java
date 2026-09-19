package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderPersistenceService(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order saveOrder(CreateOrderRequest request, List<Product> products, List<PricingCalculator.LineItem> lineItems) {
        Customer customer = customerRepository.save(new Customer(
                request.customerFullName(),
                request.customerAddress(),
                request.customerPhone()));
        Order order = orderRepository.save(new Order(customer.getId(), "new"));
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = lineItems.get(i).quantity();
            orderItemRepository.save(new OrderItem(order.getId(), product.getId(), quantity));
        }
        return order;
    }
}
