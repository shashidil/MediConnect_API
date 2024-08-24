package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByCustomerId(Long customerId);

    Optional<Order> findByorderNumber(String orderId);


    List<Order> findByPharmacistId(Long pharmacistId);
}
