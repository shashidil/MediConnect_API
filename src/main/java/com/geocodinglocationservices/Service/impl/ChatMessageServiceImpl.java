package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.ChatMessageService;
import com.geocodinglocationservices.models.ChatMessage;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.ChatRequest;
import com.geocodinglocationservices.payload.response.ChatMessageDTO;
import com.geocodinglocationservices.payload.response.UserDTO;
import com.geocodinglocationservices.repository.ChatMessageRepository;
import com.geocodinglocationservices.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    public ChatMessage saveChat(ChatRequest chatMessage) {

        User userReceiver = userRepository.findById(chatMessage.getReceiverId()).orElseThrow(() -> new UsernameNotFoundException("Receiver not found"));
        User userSender = userRepository.findById(chatMessage.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("Sender Not Found"));

        ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setReceiver(userReceiver);
        chatMessage1.setSender(userSender);
        chatMessage1.setContent(chatMessage.getContent());
       return chatMessageRepository.save(chatMessage1);
    }

    @Override
    public List<ChatMessageDTO> findMessagesByChat(Long senderId, Long receiverId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                senderId, receiverId, senderId, receiverId);

        return chatMessages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setContent(chatMessage.getContent());
        dto.setSender(convertUserToDTO(chatMessage.getSender()));
        dto.setReceiver(convertUserToDTO(chatMessage.getReceiver()));
        dto.setTimestamp(chatMessage.getTimestamp());
        return dto;
    }

    @Override
    public List<UserDTO> findDistinctChatUsers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<User> users = userRepository.findDistinctChatUsers(userId);
        return users.stream().map(this::convertUserToDTO).collect(Collectors.toList());
    }

//    private UserDTO convertToUserDtoWithMessages(User user,  Long otherUserId) {
//        // Fetch sent messages from the current user to the other user
//        List<ChatMessage> sentMessages = chatMessageRepository.findBySenderIdAndReceiverId(user.getId(), otherUserId);
//
//        // Fetch received messages from the other user to the current user
//        List<ChatMessage> receivedMessages = chatMessageRepository.findByReceiverIdAndSenderId(user.getId(), otherUserId);
//
//        List<ChatMessageDTO> sentMessageDtos = sentMessages.stream()
//                .map(this::convertToChatMessageDto)
//                .collect(Collectors.toList());
//
//        List<ChatMessageDTO> receivedMessageDtos = receivedMessages.stream()
//                .map(this::convertToChatMessageDto)
//                .collect(Collectors.toList());
//
//        return new UserDTO(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                sentMessageDtos,
//                receivedMessageDtos
//        );
//    }

    private ChatMessageDTO convertToChatMessageDto(ChatMessage chatMessage) {
        UserDTO senderDto = convertUserToDTO(chatMessage.getSender());
        UserDTO receiverDto = convertUserToDTO(chatMessage.getReceiver());

        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getContent(),
                senderDto,
                receiverDto,
                chatMessage.getTimestamp()
        );
    }

    private UserDTO convertUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
