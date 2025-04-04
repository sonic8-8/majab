package kr.kro.majab.order_item;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.item.Item;
import kr.kro.majab.order.Order;
import kr.kro.majab.order.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_items_id")
    private Long id;

    private int quantity;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "items_id")
    private Item item;

    @Builder
    public OrderItem(int quantity, int price, Order order, Item item) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.item = item;
    }

    public void changeOrder(Order order) {
        this.order = order;
        order.getOrderItems().add(this);
    }

    public void changeItem(Item item) {
        this.item = item;
        item.getOrderItems().add(this);
    }
}
