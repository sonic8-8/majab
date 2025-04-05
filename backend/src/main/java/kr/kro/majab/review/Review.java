package kr.kro.majab.review;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.order.Order;
import kr.kro.majab.owner_comment.OwnerComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviews_id")
    private Long id;

    private int star;

    private String content;

    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    private OwnerComment ownerComment;

    public Review(int star, String content, String pictureUrl, Order order, OwnerComment ownerComment) {
        this.star = star;
        this.content = content;
        this.pictureUrl = pictureUrl;
        this.order = order;
        this.ownerComment = ownerComment;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public void changeOwnerComment (OwnerComment ownerComment) {
        if (this.ownerComment != null) {
            this.ownerComment.changeReview(null);
        }
        this.ownerComment = ownerComment;

        if (ownerComment != null && ownerComment.getReview() != this) {
            ownerComment.changeReview(this);
        }
    }
}
