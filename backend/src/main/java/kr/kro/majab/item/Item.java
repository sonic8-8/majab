package kr.kro.majab.item;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.order_item.OrderItem;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    private Long id;

    private int stock;

    private int originalPrice;

    private int discountedPrice;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stores_id")
    private Store store;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Item(int stock, int originalPrice, int discountedPrice, String description) {
        this.stock = stock;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.description = description;
    }

    public Item(Store store) {
        this.store = store;
    }

    public void changeStore(Store store) {
        this.store = store;
        store.getItems().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.changeItem(this);
    }


    public void updateInfo(int originalPrice, int discountedPrice, String description) {
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.description = description;
    }

    public void updateStock(int stock) {
        this.stock = stock;
    }

    public void deductStock(int quantity) { // 주문할때 사용
        if (isStockLessThan(quantity)) {
            throw new IllegalArgumentException("차감할 재고 수가 부족합니다");
        }

        this.stock -= quantity;
    }

    public boolean isStockLessThan(int quantity) {
        return this.stock < quantity;
    }




}
