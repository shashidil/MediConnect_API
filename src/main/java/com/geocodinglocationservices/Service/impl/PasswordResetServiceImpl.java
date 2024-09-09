package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.PasswordResetService;
import com.geocodinglocationservices.models.PasswordResetToken;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.repository.PasswordResetTokenRepository;
import com.geocodinglocationservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null){
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setEmail(email);
            resetToken.setToken(token);
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // Token valid for 30 minutes
            tokenRepository.save(resetToken);

            // Send the email with the token
            emailService.sendPasswordResetEmail(email, token);
        }
        else {
            throw new RuntimeException("Please Enter Valid Email");
        }


    }

    @Override
    public String resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ("Invalid or expired token.");
        }

        User user = userRepository.findByEmail(resetToken.getEmail());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally delete the token after use
        tokenRepository.delete(resetToken);

        return ("Password reset successful.");

    }

}
