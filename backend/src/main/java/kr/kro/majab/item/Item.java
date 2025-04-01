package kr.kro.majab.item;

import jakarta.persistence.*;
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
public class Item {

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

    public void updateInfo(int originalPrice, int discountedPrice, String description) {
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.description = description;
    }

    public void updateStock(int stock) {
        this.stock = stock;
    }


}
