package com.eg.invoice.controller;

import com.eg.invoice.constant.enums.StatusEnum;
import com.eg.invoice.dao.InvoiceDetailDao;
import com.eg.invoice.dto.CreateInvoiceRequestDto;
import com.eg.invoice.dto.InvoiceDetailResponseDto;
import com.eg.invoice.dto.InvoiceResponseDto;
import com.eg.invoice.dto.PaymentOverdueInvoiceRequestDto;
import com.eg.invoice.dto.PaymentRequestDto;
import com.eg.invoice.entity.InvoiceDetailEntity;
import com.eg.invoice.repository.InvoiceDetailRepository;
import com.eg.invoice.service.InterfaceInvoiceDetailService;
import com.eg.invoice.service.impl.InvoiceDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InvoiceEndpointTest {

    @InjectMocks
    private InvoiceEndpoint invoiceEndpoint;

    @Mock
    private InterfaceInvoiceDetailService interfaceInvoiceDetailService;

    @Test
    void testCreateInvoice() {
        CreateInvoiceRequestDto createInvoiceRequestDto = new CreateInvoiceRequestDto();
        createInvoiceRequestDto.setAmount(500.00);
        createInvoiceRequestDto.setDueDate(LocalDate.now().plusDays(10));

        InvoiceResponseDto invoiceResponseDto = new InvoiceResponseDto(1L);

        when(interfaceInvoiceDetailService.createInvoice(any())).thenReturn(invoiceResponseDto);

        ResponseEntity<InvoiceResponseDto> apiResponse = invoiceEndpoint.createInvoice(createInvoiceRequestDto);
        assertNotNull(apiResponse.getStatusCode());
        assertNotNull(apiResponse.getBody());
        assertEquals(HttpStatus.CREATED, apiResponse.getStatusCode());
        assertEquals(invoiceResponseDto.getId(), apiResponse.getBody().getId());
    }

    @Test
    void getAllInvoices() {
        InvoiceDetailResponseDto invoiceDetailResponseDto = new InvoiceDetailResponseDto();
        invoiceDetailResponseDto.setId(1L);
        invoiceDetailResponseDto.setStatus(StatusEnum.PAID.getValue());
        invoiceDetailResponseDto.setAmount(1300.00);
        invoiceDetailResponseDto.setPaidAmount(0.0);
        invoiceDetailResponseDto.setDueDate(LocalDate.now().minusDays(10));

        when(interfaceInvoiceDetailService.getAllInvoices()).thenReturn(List.of(invoiceDetailResponseDto));

        ResponseEntity<List<InvoiceDetailResponseDto>> apiResponse = invoiceEndpoint.getAllInvoices();
        assertNotNull(apiResponse.getStatusCode());
        assertNotNull(apiResponse.getBody());
        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
        assertFalse(apiResponse.getBody().isEmpty());
        assertEquals(invoiceDetailResponseDto, apiResponse.getBody().get(0));
    }

    @Test
    void processPayment() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setAmount(800.00);

        InvoiceDetailResponseDto invoiceDetailResponseDto = new InvoiceDetailResponseDto();
        invoiceDetailResponseDto.setId(1L);
        invoiceDetailResponseDto.setStatus(StatusEnum.PAID.getValue());
        invoiceDetailResponseDto.setAmount(1300.00);
        invoiceDetailResponseDto.setPaidAmount(0.0);
        invoiceDetailResponseDto.setDueDate(LocalDate.now().minusDays(10));

        ResponseEntity<String> apiResponse = invoiceEndpoint.processPayment(1L, paymentRequestDto);
        assertNotNull(apiResponse.getStatusCode());
        assertNull(apiResponse.getBody());
        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }

    @Test
    void processOverduePayment() {
        PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto = new PaymentOverdueInvoiceRequestDto();
        paymentOverdueInvoiceRequestDto.setLateFee(30.00);
        paymentOverdueInvoiceRequestDto.setOverdueDays(5);

        ResponseEntity<String> apiResponse = invoiceEndpoint.processOverduePayment(paymentOverdueInvoiceRequestDto);
        assertNotNull(apiResponse.getStatusCode());
        assertNull(apiResponse.getBody());
        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }

}