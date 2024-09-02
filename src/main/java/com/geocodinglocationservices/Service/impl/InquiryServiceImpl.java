package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.InquiryService;
import com.geocodinglocationservices.models.Inquiry;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.InquiryRequestDTO;
import com.geocodinglocationservices.payload.response.InquiryResponseDTO;
import com.geocodinglocationservices.repository.InquiryRepository;
import com.geocodinglocationservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InquiryServiceImpl implements InquiryService {
    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public InquiryResponseDTO createInquiry(InquiryRequestDTO inquiryDTO) {
        User sender = userRepository.findById(inquiryDTO.getSenderId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setSubject(inquiryDTO.getSubject());
        inquiry.setMessage(inquiryDTO.getMessage());
        inquiry.setSender(sender);
        inquiry.setStatus(inquiryDTO.getStatus());

        // Save inquiry and return the response DTO
        inquiry = inquiryRepository.save(inquiry);
        return convertToResponseDTO(inquiry);
    }

    @Override
    public List<InquiryResponseDTO> getInquiriesBySenderType(Class<? extends User> senderType) {
        return inquiryRepository.findBySenderType(senderType)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InquiryResponseDTO updateInquiryStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        inquiry.setStatus(status);
        inquiry = inquiryRepository.save(inquiry);
        return convertToResponseDTO(inquiry);
    }

    private InquiryResponseDTO convertToResponseDTO(Inquiry inquiry) {
        return new InquiryResponseDTO(
                inquiry.getId(),
                inquiry.getSubject(),
                inquiry.getMessage(),
                inquiry.getStatus(),
                inquiry.getSender().getId(),
                inquiry.getCreatedDate(),
                inquiry.getLastUpdated()
        );
    }
}

