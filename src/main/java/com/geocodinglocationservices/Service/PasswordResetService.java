package com.geocodinglocationservices.Service;

import org.springframework.stereotype.Service;

public interface PasswordResetService {
     void createPasswordResetToken(String email);

     String resetPassword(String token,String newPassword);
}
