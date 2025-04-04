package kr.kro.majab.owner_review;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.owner.Owner;
import kr.kro.majab.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "owner_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_reviews_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owners_id")
    private Owner owner;

    @OneToMany(mappedBy = "ownerReview")
    private List<Review> reviews = new ArrayList<>();

    public OwnerReview(String content) {
        this.content = content;
    }

    public void changeOwner(Owner owner) {
        this.owner = owner;
        owner.getOwnerReviews().add(this);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.changeOwnerReview(this);
    }

}
