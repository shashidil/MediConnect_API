package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.ChatMessage;
import com.geocodinglocationservices.payload.request.ChatRequest;
import com.geocodinglocationservices.payload.response.ChatMessageDTO;
import com.geocodinglocationservices.payload.response.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatMessageService {

    ChatMessage saveChat(ChatRequest chatMessage);

    List<ChatMessageDTO> findMessagesByChat(Long senderId, Long receiverId);

    ChatMessageDTO convertToDTO(ChatMessage saveChat);

    List<UserDTO> findDistinctChatUsers(Long userId);
}
