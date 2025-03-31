package kr.kro.majab.review;

import jakarta.persistence.*;
import kr.kro.majab.order.Order;
import kr.kro.majab.owner_review.OwnerReview;
import kr.kro.majab.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviews_id")
    private Long id;

    private int star;

    private String content;

    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_reviews_id")
    private OwnerReview ownerReview;

    public Review(int star, String content, String pictureUrl, User user, Order order, OwnerReview ownerReview) {
        this.star = star;
        this.content = content;
        this.pictureUrl = pictureUrl;
        this.user = user;
        this.order = order;
        this.ownerReview = ownerReview;
    }
}
