package com.eg.invoice.dao;

import com.eg.invoice.constant.enums.StatusEnum;
import com.eg.invoice.entity.InvoiceDetailEntity;
import com.eg.invoice.repository.InvoiceDetailRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InvoiceDetailDaoTest {

    @InjectMocks
    private InvoiceDetailDao invoiceDetailDao;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @Test
    void testSaveInvoice() {
        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));
        when(invoiceDetailRepository.save(any())).thenReturn(invoiceDetailEntity);

        InvoiceDetailEntity saveInvoiceResponse = invoiceDetailDao.saveInvoice(invoiceDetailEntity);
        assertNotNull(saveInvoiceResponse);
        assertEquals(invoiceDetailEntity.getAmount(),saveInvoiceResponse.getAmount());
        assertEquals(invoiceDetailEntity.getStatus(),saveInvoiceResponse.getStatus());
    }

    @Test
    void testFindAllInvoices() {
        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(6L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailRepository.findAll()).thenReturn(List.of(invoiceDetailEntity));

        List<InvoiceDetailEntity> invoiceDetailResponse = invoiceDetailDao.findAllInvoices();
        assertNotNull(invoiceDetailResponse);
        assertFalse(invoiceDetailResponse.isEmpty());
        assertEquals(1, invoiceDetailResponse.size());
    }

    @Test
    void testFindInvoiceDetailById() {
        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setId(6L);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailEntity.setAmount(1300.00);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setDueDate(LocalDate.now().plusDays(10));

        when(invoiceDetailRepository.findById(any())).thenReturn(Optional.of(invoiceDetailEntity));

        Optional<InvoiceDetailEntity> invoiceDetailResponse = invoiceDetailDao.findInvoiceDetailById(6L);
        assertNotNull(invoiceDetailResponse);
        assertTrue(invoiceDetailResponse.isPresent());
        assertEquals(invoiceDetailEntity, invoiceDetailResponse.get());
    }
}