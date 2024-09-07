package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceRequest createInvoice(InvoiceRequest invoice);


    List<InvoiceResponse> getInvoiceForCustomer(Long customerId) throws Exception;

    InvoiceResponse getInvoicesByInvoiceNumber(Long invoiceId);
}
