package com.geocodinglocationservices.Service;

public interface UserFcmTokenService {
     void saveFcmToken(Long userId, String fcmToken);
     String getFcmTokenByUserId(Long userId);
}
