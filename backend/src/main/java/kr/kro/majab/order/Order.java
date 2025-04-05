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

    public static Order createOrder(LocalDateTime registeredDateTime, Item item, int quantity) {
        Order order =  Order.builder()
                .orderStatus(OrderStatus.RESERVED)
                .registeredDateTime(registeredDateTime)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .price(item.getDiscountedPrice())
                .quantity(quantity)
                .build();

        orderItem.changeItem(item);
        orderItem.changeOrder(order);

        /**
         * 가게마다 상품이 하나씩만 존재하기 때문에 하나만 추가하도록 구현함
         * 추후 가게에서 여러 상품을 판매할 수 있도록 하기 위해 List<OrderItem>으로 구현해놨음
         */
        order.addOrderItem(orderItem);

        return order;
    }

    public int calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }
}
