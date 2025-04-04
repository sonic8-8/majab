package kr.kro.majab.order;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.item.Item;
import kr.kro.majab.order_item.OrderItem;
import kr.kro.majab.review.Review;
import kr.kro.majab.store.Store;
import kr.kro.majab.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public LocalDateTime registeredDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stores_id")
    private Store store;

    @OneToMany(mappedBy = "order")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(OrderStatus orderStatus, LocalDateTime registeredDateTime) {
        this.orderStatus = orderStatus;
        this.registeredDateTime = registeredDateTime;
    }

    public void changeUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void changeStore(Store store) {
        this.store = store;
        store.getOrders().add(this);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.changeOrder(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    public static Order createOrder(LocalDateTime registeredDateTime, ArrayList<Item> items, int quantity) {
        Order order =  Order.builder()
                .orderStatus(OrderStatus.RESERVED)
                .registeredDateTime(registeredDateTime)
                .build();

        /**
         * 요구사항에서는 가게마다 상품을 한 개씩만 등록할 수 있도록 했으나
         * 추후 상품을 여러 종류 등록할 수 있도록 하기 위해
         * 미리 1:N 관계로 구현함
         */
        List<OrderItem> orderItems = items.stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .item(item)
                        .price(item.getDiscountedPrice())
                        .quantity(quantity)
                        .build())
                .toList();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    public int calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }
}
