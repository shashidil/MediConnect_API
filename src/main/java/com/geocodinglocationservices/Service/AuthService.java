package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.SignupRequest;
import org.springframework.stereotype.Service;


public interface AuthService {
    User registerUser(SignupRequest signUpRequest);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
