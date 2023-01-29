package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.increff.pos.entity.GeneratorTable.*;

@Getter
@Setter
@Entity
@Table(name = "pos_orders")
public class OrderPojo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_ORDER_TABLE)
    @TableGenerator(name = POS_ORDER_TABLE, table = POS_TABLE_NAME, allocationSize = 1, pkColumnName = POS_PK_COLUMN_NAME, valueColumnName = POS_PK_COLUMN_VALUE)
    private Integer id;

}
