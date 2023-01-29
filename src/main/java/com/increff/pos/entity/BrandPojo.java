package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import static com.increff.pos.entity.GeneratorTable.*;

@Getter
@Setter
@Entity
@Table(
        name = "pos_brands",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category"})}
)
public class BrandPojo extends BaseEntity {
    //    TODO check if names of generator table are correct
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_BRAND_TABLE)
    @TableGenerator(name = POS_BRAND_TABLE, table = POS_TABLE_NAME, allocationSize = 1, pkColumnName = POS_PK_COLUMN_NAME, valueColumnName = POS_PK_COLUMN_VALUE)
    private Integer id;
    @Column(nullable = false)
    @Length(min = 1)
    private String name;
    @Column(nullable = false)
    @Length(min = 1)
    private String category;
}
