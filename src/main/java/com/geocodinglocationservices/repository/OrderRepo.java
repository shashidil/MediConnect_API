package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByCustomerId(Long customerId);

    Optional<Order> findByorderNumber(String orderId);


    List<Order> findByPharmacistId(Long pharmacistId);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :fromDate AND :toDate")
    List<Order> findByOrderDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT new com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO(DAY(o.orderDate), COUNT(o.id)) " +
            "FROM Order o WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year " +
            "GROUP BY DAY(o.orderDate)")
    List<OrderQuantityByDayDTO> findOrderQuantitiesByMonth(int month, int year);


}
