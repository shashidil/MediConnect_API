package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken,Long> {
    Optional<UserFcmToken> findByUserId(Long userId);
}
