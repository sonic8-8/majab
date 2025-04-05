package kr.kro.majab.owner_comment;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.owner.Owner;
import kr.kro.majab.review.Review;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "owner_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_comments_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stores_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    private Review review;

    public OwnerComment(String content) {
        this.content = content;
    }

    public void changeReview(Review review) {
        this.review = review;
    }
}
