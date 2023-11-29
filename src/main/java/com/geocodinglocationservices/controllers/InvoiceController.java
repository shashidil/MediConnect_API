package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.InvoiceService;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    @PostMapping("/")
    public ResponseEntity<InvoiceRequest> createInvoice(@RequestBody InvoiceRequest invoice) {
        InvoiceRequest createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long invoiceId,
                                                      @RequestParam Long customerId) throws Exception {
        InvoiceResponse invoice = invoiceService.getInvoiceForCustomer(invoiceId, customerId);
        return ResponseEntity.ok(invoice);
    }
}
