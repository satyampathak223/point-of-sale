package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private Integer InvoiceNumber;
    private String InvoiceDate;

    private String InvoiceTime;

    private Double Total;

    private List<InvoiceItem> LineItems;

    public Double getTotal() {
        Total = 0.0;
        for (InvoiceItem item : LineItems) {
            Total += item.getTotal();
        }
        return Total;
    }
}
