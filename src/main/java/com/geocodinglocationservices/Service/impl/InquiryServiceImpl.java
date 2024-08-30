package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.InquiryService;
import com.geocodinglocationservices.models.Inquiry;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.InquiryRequestDTO;
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
    public InquiryRequestDTO createInquiry(InquiryRequestDTO inquiryDTO) {
        User sender = userRepository.findById(inquiryDTO.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setSubject(inquiryDTO.getSubject());
        inquiry.setMessage(inquiryDTO.getMessage());
        inquiry.setSender(sender);
        inquiry = inquiryRepository.save(inquiry);

        inquiryDTO.setId(inquiry.getId());
        return inquiryDTO;
    }

    @Override
    public List<InquiryRequestDTO> getInquiriesBySenderType(Class<? extends User> senderType) {
        return inquiryRepository.findBySenderType(senderType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InquiryRequestDTO updateInquiryStatus(Long id, String status) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inquiry not found"));
        inquiry.setStatus(status);
        inquiryRepository.save(inquiry);
        return convertToDTO(inquiry);
    }

    private InquiryRequestDTO convertToDTO(Inquiry inquiry) {
        return new InquiryRequestDTO(
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
