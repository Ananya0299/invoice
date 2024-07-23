package com.eg.invoice.dao;

import com.eg.invoice.entity.InvoiceDetailEntity;
import com.eg.invoice.repository.InvoiceDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InvoiceDetailDao {

    private final InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceDetailEntity saveInvoice(InvoiceDetailEntity invoiceEntity) {
        return invoiceDetailRepository.save(invoiceEntity);
    }

    public List<InvoiceDetailEntity> findAllInvoices() {
        return invoiceDetailRepository.findAll();
    }

    public Optional<InvoiceDetailEntity> findInvoiceDetailById(Long id) {
        return invoiceDetailRepository.findById(id);
    }

}
