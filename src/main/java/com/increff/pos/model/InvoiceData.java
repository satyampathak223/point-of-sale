package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private Integer invoiceNumber;
    private String invoiceDate;

    private String invoiceTime;

    private List<InvoiceItem> lineItems;

    public Double getTotal() {
        Double Total = 0.0;
        for (InvoiceItem item : lineItems) {
            Total += item.getTotal();
        }
        return Total;
    }
}
