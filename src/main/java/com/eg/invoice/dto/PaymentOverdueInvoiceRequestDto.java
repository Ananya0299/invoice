package com.eg.invoice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOverdueInvoiceRequestDto {

    @NotNull
    private Double lateFee;

    @Min(1)
    @NotNull
    private Integer overdueDays;

}
