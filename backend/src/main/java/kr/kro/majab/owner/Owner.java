package kr.kro.majab.owner;

import jakarta.persistence.*;
import kr.kro.majab.notice.Notice;
import kr.kro.majab.owner_review.OwnerReview;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "owners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owners_id")
    private Long id;

    private String email;

    private String password;

    private String phoneNumber;

    @OneToMany(mappedBy = "owner")
    private List<OwnerReview> ownerReviews = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Store> stores = new ArrayList<>();
}
