package kr.kro.majab.order;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
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

    public static Order createOrder(OrderStatus orderStatus, LocalDateTime registeredDateTime, OrderItem... orderItems) {
        Order order =  Order.builder()
                .orderStatus(OrderStatus.RESERVED)
                .registeredDateTime(LocalDateTime.now())
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }
}
