package com.rbdip.bookstore.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricingCalculatorTest {

    private PricingCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PricingCalculator();
    }

    @Test
    void appliesVipDiscount() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 1)), "vip", null);

        assertThat(total).isEqualByComparingTo("90.00");
    }

    @Test
    void appliesWholesaleDiscount() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 1)), "wholesale", null);

        assertThat(total).isEqualByComparingTo("85.00");
    }

    @Test
    void appliesVolumeDiscountWhenQuantityExceedsTen() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 11)), "regular", null);

        assertThat(total).isEqualByComparingTo("104.50");
    }

    @Test
    void doesNotApplyVolumeDiscountAtExactlyTen() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 10)), "regular", null);

        assertThat(total).isEqualByComparingTo("100.00");
    }

    @Test
    void appliesSave10Coupon() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("50.00"), 1)), "regular", "SAVE10");

        assertThat(total).isEqualByComparingTo("40.00");
    }

    @Test
    void appliesSave20PercentCoupon() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("50.00"), 1)), "regular", "SAVE20PERCENT");

        assertThat(total).isEqualByComparingTo("40.00");
    }

    @Test
    void floorsNegativeTotalToZero() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("5.00"), 1)), "regular", "SAVE10");

        assertThat(total).isEqualByComparingTo("0.00");
    }

    @Test
    void appliesHighTotalDiscountAboveThousand() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 11)), "regular", null);

        assertThat(total).isEqualByComparingTo("1024.10");
    }

    @Test
    void doesNotApplyHighTotalDiscountAtExactlyThousand() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 10)), "regular", null);

        assertThat(total).isEqualByComparingTo("1000.00");
    }

    @Test
    void combinesMultipleLineItems() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(
                        new PricingCalculator.LineItem(new BigDecimal("10.00"), 2),
                        new PricingCalculator.LineItem(new BigDecimal("5.00"), 3)),
                "regular",
                null);

        assertThat(total).isEqualByComparingTo("35.00");
    }

    @Test
    void vipTakesPrecedenceOverRegularButNotWholesaleBranch() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 1)), "vip", "SAVE10");

        assertThat(total).isEqualByComparingTo("80.00");
    }

    @Test
    void treatsNullCustomerTypeAsRegular() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("20.00"), 1)), null, null);

        assertThat(total).isEqualByComparingTo("20.00");
    }

    @Test
    void ignoresUnknownCouponCode() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("20.00"), 1)), "regular", "UNKNOWN");

        assertThat(total).isEqualByComparingTo("20.00");
    }

    @Test
    void appliesWholesaleThenHighTotalDiscount() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 15)), "wholesale", null);

        assertThat(total).isEqualByComparingTo("1187.03");
    }

    @Test
    void returnsZeroForEmptyItems() {
        BigDecimal total = calculator.calculateOrderTotal(List.of(), "regular", null);

        assertThat(total).isEqualByComparingTo("0.00");
    }
}
