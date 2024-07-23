package com.eg.invoice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequestDto {

    @NotNull
    private Double amount;

}
