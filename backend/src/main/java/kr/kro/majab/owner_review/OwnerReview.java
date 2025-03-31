package kr.kro.majab.owner_review;

import jakarta.persistence.*;
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
public class OwnerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_reviews_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owners_id")
    private Owner owner;

    private String content;

    @OneToMany(mappedBy = "ownerReview")
    private List<Review> reviews = new ArrayList<>();

    public OwnerReview(String content) {
        this.content = content;
    }

}
