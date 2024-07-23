package com.eg.invoice.exception;

public class InvoiceNotExistException extends RuntimeException{
    public InvoiceNotExistException(String message) {
        super(message);
    }
}
