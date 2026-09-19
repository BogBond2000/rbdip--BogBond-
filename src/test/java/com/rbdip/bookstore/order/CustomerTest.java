package com.rbdip.bookstore.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void splitsFullNameIntoFirstAndLast() {
        Customer customer = new Customer("Ivan Petrov", "Moscow", "+79990000000");

        assertThat(customer.getFirstName()).isEqualTo("Ivan");
        assertThat(customer.getLastName()).isEqualTo("Petrov");
        assertThat(customer.getFullName()).isEqualTo("Ivan Petrov");
    }

    @Test
    void handlesSingleWordName() {
        Customer customer = new Customer("Madonna", null, null);

        assertThat(customer.getFirstName()).isEqualTo("Madonna");
        assertThat(customer.getLastName()).isEmpty();
        assertThat(customer.getFullName()).isEqualTo("Madonna");
    }

    @Test
    void handlesNullFullName() {
        Customer customer = new Customer(null, "Address", null);

        assertThat(customer.getFirstName()).isEmpty();
        assertThat(customer.getLastName()).isEmpty();
        assertThat(customer.getFullName()).isEmpty();
    }
}
