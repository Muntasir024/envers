package com.example.envers.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductAudit {
    private Product product;
    private String username;
    private String ip;
    private Date revisionDate;

    public ProductAudit(Product product, String username, String ip, Date revisionDate) {
        this.product = product;
        this.username = username;
        this.ip = ip;
        this.revisionDate = revisionDate;
    }
}

