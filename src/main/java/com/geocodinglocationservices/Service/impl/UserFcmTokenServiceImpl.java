package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.UserFcmTokenService;
import com.geocodinglocationservices.models.UserFcmToken;
import com.geocodinglocationservices.repository.UserFcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFcmTokenServiceImpl implements UserFcmTokenService {
    @Autowired
    UserFcmTokenRepository userFcmTokenRepository;
    @Override
    public void saveFcmToken(Long userId, String fcmToken) {
        Optional<UserFcmToken> existingToken = userFcmTokenRepository.findByUserId(userId);
        if (existingToken.isPresent()) {
            UserFcmToken userFcmToken = existingToken.get();
            userFcmToken.setFcmToken(fcmToken);
            userFcmTokenRepository.save(userFcmToken);
        } else {
            UserFcmToken newUserFcmToken = new UserFcmToken(userId, fcmToken);
            userFcmTokenRepository.save(newUserFcmToken);
        }
    }
@Override
    public String getFcmTokenByUserId(Long userId) {
        return userFcmTokenRepository.findByUserId(userId)
                .map(UserFcmToken::getFcmToken)
                .orElse(null);
    }
}
