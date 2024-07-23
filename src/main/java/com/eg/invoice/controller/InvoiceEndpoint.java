package com.eg.invoice.controller;

import com.eg.invoice.dto.CreateInvoiceRequestDto;
import com.eg.invoice.dto.InvoiceDetailResponseDto;
import com.eg.invoice.dto.InvoiceResponseDto;
import com.eg.invoice.dto.PaymentOverdueInvoiceRequestDto;
import com.eg.invoice.dto.PaymentRequestDto;
import com.eg.invoice.service.InterfaceInvoiceDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/invoices")
@RestController
public class InvoiceEndpoint {

    private final InterfaceInvoiceDetailService interfaceInvoiceDetailService;

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> createInvoice(@Valid @RequestBody CreateInvoiceRequestDto createInvoiceRequestDto) {
        return new ResponseEntity<>(interfaceInvoiceDetailService.createInvoice(createInvoiceRequestDto),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDetailResponseDto>> getAllInvoices() {
        return new ResponseEntity<>(interfaceInvoiceDetailService.getAllInvoices(),HttpStatus.OK);
    }

    @PostMapping(value = "/{invoiceId}/payments")
    public ResponseEntity<String> processPayment(@PathVariable(value = "invoiceId") Long id, @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        interfaceInvoiceDetailService.processPayment(id,paymentRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/process-overdue")
    public ResponseEntity<String> processOverduePayment(@Valid @RequestBody PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto) {
        interfaceInvoiceDetailService.processOverduePayment(paymentOverdueInvoiceRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
