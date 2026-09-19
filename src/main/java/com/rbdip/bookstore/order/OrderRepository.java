package com.rbdip.bookstore.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.items i "
            + "LEFT JOIN FETCH i.product "
            + "LEFT JOIN FETCH o.customer")
    List<Order> findAllWithItemsAndCustomer();
}
