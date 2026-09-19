package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final OrderLineItemResolver lineItemResolver;
    private final OrderPersistenceService persistenceService;
    private final OrderNotificationService notificationService;
    private final PricingCalculator pricingCalculator = new PricingCalculator();

    public OrderService(
            OrderValidator orderValidator,
            OrderLineItemResolver lineItemResolver,
            OrderPersistenceService persistenceService,
            OrderNotificationService notificationService) {
        this.orderValidator = orderValidator;
        this.lineItemResolver = lineItemResolver;
        this.persistenceService = persistenceService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        orderValidator.validate(request);

        OrderLineItemResolver.ResolvedLineItems resolved = lineItemResolver.resolve(request);
        String customerType = request.customerType() == null ? "regular" : request.customerType();
        BigDecimal total = pricingCalculator.calculateOrderTotal(
                resolved.lineItems(), customerType, request.couponCode());

        Order order = persistenceService.saveOrder(request, resolved.products(), resolved.lineItems());
        notificationService.sendConfirmationEmail(request.customerFullName(), order.getId(), total);

        return order;
    }
}
