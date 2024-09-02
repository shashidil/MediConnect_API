package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.InquiryRequestDTO;
import com.geocodinglocationservices.payload.response.InquiryResponseDTO;

import java.util.List;

public interface InquiryService {
    InquiryResponseDTO createInquiry(InquiryRequestDTO inquiryDTO);

    List<InquiryResponseDTO> getInquiriesBySenderType(Class<? extends User> senderType);

    InquiryResponseDTO updateInquiryStatus(Long id, String status);
}
