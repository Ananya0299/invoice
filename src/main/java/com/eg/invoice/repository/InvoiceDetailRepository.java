package com.eg.invoice.repository;


import com.eg.invoice.entity.InvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, Long> {

}
