package com.eg.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {

    private LocalDateTime timestamp;
    private Integer httpStatus;
    private String exception;
    private String message;

}
