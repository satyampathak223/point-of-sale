package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import static com.increff.pos.entity.GeneratorTable.*;

// TODO naming strategies
@Getter
@Setter
@Entity
@Table(
        name = "pos_products",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"barcode"})}
)
@Check(constraints = "mrp >= 0")
public class ProductPojo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_PRODUCT_TABLE)
    @TableGenerator(name = POS_PRODUCT_TABLE, table = POS_TABLE_NAME, allocationSize = 1, pkColumnName = POS_PK_COLUMN_NAME, valueColumnName = POS_PK_COLUMN_VALUE)
    Integer id;

    @Column(nullable = false)
    @Length(min = 1)
    String barcode;
    @Column(name = "brand_category_id", nullable = false)
    Integer brandCategoryId;
    @Column(nullable = false)
    @Length(min = 1)
    String name;
    @Column(name = "mrp", nullable = false)
    Double MRP;
}
