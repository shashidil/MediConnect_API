package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.InvoiceService;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
   // @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    @PostMapping()
    public ResponseEntity<InvoiceRequest> createInvoice(@RequestBody InvoiceRequest invoice) {
        InvoiceRequest createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesForCustomer(@PathVariable Long customerId) throws Exception {
        List<InvoiceResponse> invoices = invoiceService.getInvoiceForCustomer(customerId);
        return ResponseEntity.ok(invoices);
    }
}
