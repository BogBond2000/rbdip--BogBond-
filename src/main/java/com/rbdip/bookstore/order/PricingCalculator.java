package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PricingCalculator {

    private static final int VOLUME_DISCOUNT_THRESHOLD = 10;
    private static final BigDecimal VOLUME_DISCOUNT_MULTIPLIER = new BigDecimal("0.95");
    private static final BigDecimal VIP_DISCOUNT_MULTIPLIER = new BigDecimal("0.9");
    private static final BigDecimal WHOLESALE_DISCOUNT_MULTIPLIER = new BigDecimal("0.85");
    private static final BigDecimal COUPON_PERCENT_MULTIPLIER = new BigDecimal("0.8");
    private static final BigDecimal HIGH_TOTAL_THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal HIGH_TOTAL_DISCOUNT_MULTIPLIER = new BigDecimal("0.98");
    private static final String CUSTOMER_TYPE_VIP = "vip";
    private static final String CUSTOMER_TYPE_WHOLESALE = "wholesale";
    private static final String COUPON_SAVE10 = "SAVE10";
    private static final String COUPON_SAVE20PERCENT = "SAVE20PERCENT";

    public record LineItem(BigDecimal price, int quantity) {
    }

    public BigDecimal calculateOrderTotal(List<LineItem> items, String customerType, String couponCode) {
        BigDecimal total = BigDecimal.ZERO;

        for (LineItem item : items) {
            BigDecimal linePrice = item.price().multiply(BigDecimal.valueOf(item.quantity()));
            if (item.quantity() > VOLUME_DISCOUNT_THRESHOLD) {
                linePrice = linePrice.multiply(VOLUME_DISCOUNT_MULTIPLIER);
            }
            total = total.add(linePrice);
        }

        if (CUSTOMER_TYPE_VIP.equals(customerType)) {
            total = total.multiply(VIP_DISCOUNT_MULTIPLIER);
        } else if (CUSTOMER_TYPE_WHOLESALE.equals(customerType)) {
            total = total.multiply(WHOLESALE_DISCOUNT_MULTIPLIER);
        }

        if (COUPON_SAVE10.equals(couponCode)) {
            total = total.subtract(BigDecimal.TEN);
        } else if (COUPON_SAVE20PERCENT.equals(couponCode)) {
            total = total.multiply(COUPON_PERCENT_MULTIPLIER);
        }

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        if (total.compareTo(HIGH_TOTAL_THRESHOLD) > 0) {
            total = total.multiply(HIGH_TOTAL_DISCOUNT_MULTIPLIER);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
