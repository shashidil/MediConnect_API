package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.Service.ChatMessageService;
import com.geocodinglocationservices.Service.CustomerService;
import com.geocodinglocationservices.models.ChatMessage;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.ChatRequest;
import com.geocodinglocationservices.payload.response.ChatMessageDTO;
import com.geocodinglocationservices.payload.response.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    @SendToUser("/queue/private-messages")
    public ChatMessageDTO sendMessage(ChatRequest chatMessage, Principal principal) {
        ChatMessage saveChat = chatMessageService.saveChat(chatMessage);// Save the message to the database
// Optionally check if the receiver is online and send a notification
        return chatMessageService.convertToDTO(saveChat);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage, Principal principal) {
        // Handle user joining a chat
        return chatMessage;
    }

    // REST API to retrieve messages between two users
    @GetMapping("/api/chats/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        List<ChatMessageDTO> chatMessages = chatMessageService.findMessagesByChat(senderId, receiverId);
        return ResponseEntity.ok(chatMessages);

    }
    // REST API to get the list of active chats for a user
    @GetMapping("/api/chats")
    public List<UserDTO> getChatList(@RequestParam Long userId) {
        return chatMessageService.findDistinctChatUsers(userId);
    }

}
