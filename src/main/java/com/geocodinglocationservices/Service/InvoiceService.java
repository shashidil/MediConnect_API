package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;

public interface InvoiceService {

    InvoiceRequest createInvoice(InvoiceRequest invoice);


    InvoiceResponse getInvoiceForCustomer(Long invoiceId, Long customerId) throws Exception;
}
