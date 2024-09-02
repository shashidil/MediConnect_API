package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.payload.request.UserUpdateRequest;
import com.geocodinglocationservices.payload.response.UserDTO;
import com.geocodinglocationservices.payload.response.UserDetailsDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface AuthService {
    User registerUser(SignupRequest signUpRequest) throws IOException;

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    List<UserDTO> findDistinctChatUsers(Long userId);

    UserDTO updateUser(Long userId, UserUpdateRequest updateRequest) throws IOException;

    void handleNotificationInLogin(Long userId);

    UserDetailsDto getUserDetails(Long userId);
}
