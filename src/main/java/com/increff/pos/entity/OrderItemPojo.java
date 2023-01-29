package com.increff.pos.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.*;

import static com.increff.pos.entity.GeneratorTable.*;

@Getter
@Setter
@Entity
@Table(name = "pos_order_items")
@Check(constraints = "quantity > 0 AND selling_price >= 0 ")
public class OrderItemPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_ORDER_ITEM_TABLE)
    @TableGenerator(name = POS_ORDER_ITEM_TABLE, table = POS_TABLE_NAME, allocationSize = 1, pkColumnName = POS_PK_COLUMN_NAME, valueColumnName = POS_PK_COLUMN_VALUE)
    private Integer id;
    @Column(nullable = false)
    private Integer orderId;
    @Column(nullable = false)
    private Integer productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, name = "selling_price")
    private Double sellingPrice;
}
