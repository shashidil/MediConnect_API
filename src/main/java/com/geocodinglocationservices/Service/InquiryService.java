package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.InquiryRequestDTO;

import java.util.List;

public interface InquiryService {
    InquiryRequestDTO createInquiry(InquiryRequestDTO inquiryDTO);

    List<InquiryRequestDTO> getInquiriesBySenderType(Class<? extends User> senderType);

    InquiryRequestDTO updateInquiryStatus(Long id, String status);
}
