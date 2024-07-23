package com.eg.invoice.service.impl;

import com.eg.invoice.constant.enums.StatusEnum;
import com.eg.invoice.dao.InvoiceDetailDao;
import com.eg.invoice.dto.CreateInvoiceRequestDto;
import com.eg.invoice.dto.InvoiceDetailResponseDto;
import com.eg.invoice.dto.InvoiceResponseDto;
import com.eg.invoice.dto.PaymentOverdueInvoiceRequestDto;
import com.eg.invoice.dto.PaymentRequestDto;
import com.eg.invoice.entity.InvoiceDetailEntity;
import com.eg.invoice.exception.InvoiceNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InvoiceDetailServiceImplTest {

    @InjectMocks
    private InvoiceDetailServiceImpl invoiceServiceImpl;

    @Mock
    private InvoiceDetailDao invoiceDetailDao;

    @Test
    void testCreateInvoice() {
        CreateInvoiceRequestDto createInvoiceRequestDto = new CreateInvoiceRequestDto();
        createInvoiceRequestDto.setAmount(1300.00);
        createInvoiceRequestDto.setDueDate(LocalDate.now().plusDays(10));

        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(6L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailDao.saveInvoice(any())).thenReturn(invoiceDetailEntity);

        InvoiceResponseDto invoiceResponse = invoiceServiceImpl.createInvoice(createInvoiceRequestDto);
        assertNotNull(invoiceResponse);
        assertEquals(6L, invoiceResponse.getId());
    }

    @Test
    void getAllInvoices() {
        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(1L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        InvoiceDetailEntity invoiceDetailEntity1 = new InvoiceDetailEntity();
        invoiceDetailEntity1.setId(2L);
        invoiceDetailEntity1.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity1.setAmount(1800.00);
        invoiceDetailEntity1.setPaidAmount(0.0);
        invoiceDetailEntity1.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailDao.findAllInvoices()).thenReturn(List.of(invoiceDetailEntity, invoiceDetailEntity1));

        List<InvoiceDetailResponseDto> invoiceResponse = invoiceServiceImpl.getAllInvoices();
        assertNotNull(invoiceResponse);
        assertFalse(invoiceResponse.isEmpty());
        assertEquals(2, invoiceResponse.size());
        assertEquals(1, invoiceResponse.get(0).getId());
    }

    @Test
    void processPaymentPartialPay() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setAmount(500.00);

        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(1L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailDao.findInvoiceDetailById(any())).thenReturn(Optional.of(invoiceDetailEntity));

        when(invoiceDetailDao.saveInvoice(any())).thenReturn(invoiceDetailEntity);

        invoiceServiceImpl.processPayment(1L, paymentRequestDto);
        verify(invoiceDetailDao, times(1)).findInvoiceDetailById(any());
        verify(invoiceDetailDao, times(1)).saveInvoice(any());
    }

    @Test
    void processPaymentFullPay() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setAmount(1300.00);

        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(1L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailDao.findInvoiceDetailById(any())).thenReturn(Optional.of(invoiceDetailEntity));

        when(invoiceDetailDao.saveInvoice(any())).thenReturn(invoiceDetailEntity);

        invoiceServiceImpl.processPayment(1L, paymentRequestDto);
        verify(invoiceDetailDao, times(1)).findInvoiceDetailById(any());
        verify(invoiceDetailDao, times(1)).saveInvoice(any());
    }


    @Test
    void processPaymentInvoiceNotExist() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setAmount(1300.00);

        when(invoiceDetailDao.findInvoiceDetailById(any())).thenReturn(Optional.empty());

        assertThrows(InvoiceNotExistException.class, () -> invoiceServiceImpl.processPayment(1L, paymentRequestDto));
    }

    @Test
    void processOverduePayment() {
        PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto = new PaymentOverdueInvoiceRequestDto();
        paymentOverdueInvoiceRequestDto.setLateFee(30.00);
        paymentOverdueInvoiceRequestDto.setOverdueDays(8);

        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(1L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().minusDays(10));

        InvoiceDetailEntity invoiceDetailEntity1 = new InvoiceDetailEntity();
        invoiceDetailEntity1.setId(2L);
        invoiceDetailEntity1.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity1.setAmount(1800.00);
        invoiceDetailEntity1.setPaidAmount(700.00);
        invoiceDetailEntity1.setDueDate(LocalDate.now().minusDays(10));

        when(invoiceDetailDao.findAllInvoices()).thenReturn(List.of(invoiceDetailEntity, invoiceDetailEntity1));

        invoiceServiceImpl.processOverduePayment(paymentOverdueInvoiceRequestDto);
        verify(invoiceDetailDao, times(1)).findAllInvoices();
    }

    @Test
    void processOverduePaymentNotFound() {
        PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto = new PaymentOverdueInvoiceRequestDto();
        paymentOverdueInvoiceRequestDto.setLateFee(30.00);
        paymentOverdueInvoiceRequestDto.setOverdueDays(8);

        when(invoiceDetailDao.findAllInvoices()).thenReturn(List.of());
        assertThrows(InvoiceNotExistException.class, () -> invoiceServiceImpl.processOverduePayment(paymentOverdueInvoiceRequestDto));
    }

}