package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "pos_products")
public class ProductPojo {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;
        String barcode;
        @Column(name="brand_cateory_id",nullable = false)
        Integer brandCategoryId;
        @Column(nullable = false)
        String name;
        @Column(name="mrp",nullable = false)
        Double MRP;
}
