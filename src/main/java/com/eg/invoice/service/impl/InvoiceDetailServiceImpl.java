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
import com.eg.invoice.service.InterfaceInvoiceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvoiceDetailServiceImpl implements InterfaceInvoiceDetailService {

    private final InvoiceDetailDao invoiceDetailDao;

    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceRequestDto createInvoiceRequestDto) {
        InvoiceDetailEntity invoice = new InvoiceDetailEntity();
        invoice.setAmount(createInvoiceRequestDto.getAmount());
        invoice.setDueDate(createInvoiceRequestDto.getDueDate());
        invoice.setPaidAmount(0.0);
        invoice.setStatus(StatusEnum.PENDING);
        invoice = invoiceDetailDao.saveInvoice(invoice);

        return new InvoiceResponseDto(invoice.getId());
    }

    @Override
    public List<InvoiceDetailResponseDto> getAllInvoices() {
        return invoiceDetailDao.findAllInvoices().stream()
                .map(invoice -> new InvoiceDetailResponseDto(
                        invoice.getId(),
                        invoice.getAmount(),
                        invoice.getPaidAmount(),
                        invoice.getDueDate(),
                        invoice.getStatus().getValue()
                ))
                .toList();
    }

    @Override
    public void processPayment(Long id, PaymentRequestDto paymentRequestDto) {
        // Fetch invoice and validate existence
        InvoiceDetailEntity invoiceDetailEntity = fetchInvoice(id);

        Double totalPaidAmount = invoiceDetailEntity.getPaidAmount() + paymentRequestDto.getAmount();
        invoiceDetailEntity.setPaidAmount(totalPaidAmount);
        if (totalPaidAmount.equals(invoiceDetailEntity.getAmount())) {
            invoiceDetailEntity.setStatus(StatusEnum.PAID);
        }
        invoiceDetailDao.saveInvoice(invoiceDetailEntity);
    }

    private InvoiceDetailEntity fetchInvoice(Long id) {
        return invoiceDetailDao.findInvoiceDetailById(id)
                .orElseThrow(() -> new InvoiceNotExistException("Invoice not exist"));
    }

    @Override
    public void processOverduePayment(PaymentOverdueInvoiceRequestDto paymentOverdueInvoiceRequestDto) {

        LocalDate overdueDate = LocalDate.now().minusDays(paymentOverdueInvoiceRequestDto.getOverdueDays());
        List<InvoiceDetailEntity> invoiceDetailList = invoiceDetailDao.findAllInvoices().stream()
                .filter(invoice -> StatusEnum.PENDING.equals(invoice.getStatus()) &&
                        invoice.getDueDate().isBefore(overdueDate))
                .toList();

        if (CollectionUtils.isEmpty(invoiceDetailList)) {
            throw new InvoiceNotExistException("No overdue invoices exist");
        }

        processLatePayments(paymentOverdueInvoiceRequestDto.getLateFee(),
                paymentOverdueInvoiceRequestDto.getOverdueDays(),
                invoiceDetailList,
                LocalDate.now());
    }

    private void processLatePayments(Double lateFee, Integer overdueDays, List<InvoiceDetailEntity> invoiceList, LocalDate currentDate) {
        invoiceList.forEach(invoice -> {
            Double remainingAmount = invoice.getAmount() - invoice.getPaidAmount();
            StatusEnum newStatus = invoice.getPaidAmount() > 0 && invoice.getPaidAmount() < invoice.getAmount()
                    ? StatusEnum.PAID
                    : StatusEnum.VOID;

            invoice.setStatus(newStatus);

            saveInvoiceDetail(remainingAmount + lateFee, currentDate.plusDays(overdueDays));
            invoiceDetailDao.saveInvoice(invoice);
        });
    }

    private void saveInvoiceDetail(Double amount, LocalDate dueDate) {
        InvoiceDetailEntity invoiceDetailEntity = new InvoiceDetailEntity();
        invoiceDetailEntity.setAmount(amount);
        invoiceDetailEntity.setDueDate(dueDate);
        invoiceDetailEntity.setPaidAmount(0.0);
        invoiceDetailEntity.setStatus(StatusEnum.PENDING);
        invoiceDetailDao.saveInvoice(invoiceDetailEntity);
    }
}
