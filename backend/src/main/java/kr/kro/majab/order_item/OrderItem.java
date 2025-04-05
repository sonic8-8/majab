package kr.kro.majab.order_item;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.item.Item;
import kr.kro.majab.order.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    public OrderItem(int quantity, int price) {
        this.quantity = quantity;
        this.price = price;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public void changeItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity
                && price == orderItem.price
                && Objects.equals(id, orderItem.id)
                && Objects.equals(order, orderItem.order)
                && Objects.equals(item, orderItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, price, order, item);
    }
}
