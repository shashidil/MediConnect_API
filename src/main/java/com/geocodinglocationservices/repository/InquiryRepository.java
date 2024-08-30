package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Inquiry;
import com.geocodinglocationservices.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {
    @Query("SELECT i FROM Inquiry i WHERE TYPE(i.sender) = :senderType")
    List<Inquiry> findBySenderType(Class<? extends User> senderType);
}
