package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "pos_brands")
public class BrandPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "brands-table-generator")
    @TableGenerator(name = "brands-table-generator", table = "pos_brand_ids", allocationSize = 1, pkColumnName = "brand_id", valueColumnName = "brand_value")
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
}
