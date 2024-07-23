package com.eg.invoice.service;

import com.eg.invoice.dto.CreateInvoiceRequestDto;
import com.eg.invoice.dto.InvoiceDetailResponseDto;
import com.eg.invoice.dto.InvoiceResponseDto;
import com.eg.invoice.dto.PaymentOverdueInvoiceRequestDto;
import com.eg.invoice.dto.PaymentRequestDto;

import java.util.List;

public interface InterfaceInvoiceDetailService {

    InvoiceResponseDto createInvoice(CreateInvoiceRequestDto createInvoiceRequestDto);

    List<InvoiceDetailResponseDto> getAllInvoices();

    void processPayment(Long id, PaymentRequestDto paymentRequestDto);

    void processOverduePayment(PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto);
}
