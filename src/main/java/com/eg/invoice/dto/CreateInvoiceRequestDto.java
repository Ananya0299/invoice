package com.eg.invoice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateInvoiceRequestDto {

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate dueDate;

}
