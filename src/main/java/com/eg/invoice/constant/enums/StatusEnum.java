package com.eg.invoice.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {

    PENDING("pending"),
    PAID("paid"),
    VOID("void");

    private final String value;
}
