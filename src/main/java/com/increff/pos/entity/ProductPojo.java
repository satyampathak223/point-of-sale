package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// TODO naming strategies
@Getter
@Setter
@Entity
@Table(name = "pos_products")
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "products-table-generator")
    @TableGenerator(name = "products-table-generator", table = "pos_product_ids", allocationSize = 1, pkColumnName = "product_id", valueColumnName = "product_value")
    Integer id;

    @Column(nullable = false, unique = true)
    String barcode;
    @Column(name = "brand_category_id", nullable = false)
    Integer brandCategoryId;
    @Column(nullable = false)
    String name;
    @Column(name = "mrp", nullable = false)
    Double MRP;
}
