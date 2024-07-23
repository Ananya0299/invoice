package com.eg.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailResponseDto {

    private Long id;
    private Double amount;
    private Double paidAmount;
    private LocalDate dueDate;
    private String status;

}
