package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.UserFcmTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userfcmtoken")
public class UserFcmTokenController {
    @Autowired
    private UserFcmTokenService userFcmTokenService;

    @PostMapping("/{userId}")
    public void saveFcmToken(@PathVariable Long userId, @RequestBody String fcmToken) {
        userFcmTokenService.saveFcmToken(userId, fcmToken);
    }

    @GetMapping("/{userId}")
    public String getFcmToken(@PathVariable Long userId) {
        return userFcmTokenService.getFcmTokenByUserId(userId);
    }
}
