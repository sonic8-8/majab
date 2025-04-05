package kr.kro.majab.owner;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.notice.Notice;
import kr.kro.majab.owner_review.OwnerReview;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "owners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {

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

    @Builder
    public Owner(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void addOwnerReview(OwnerReview ownerReview) {
        ownerReviews.add(ownerReview);
        ownerReview.changeOwner(this);
    }

    public void addStore(Store store) {
        stores.add(store);
        store.changeOwner(this);
    }

    public void addNotice(Notice notice) {
        notices.add(notice);
        notice.changeOwner(this);
    }

    public static Owner create() {
        return Owner.builder()
                .email("email")
                .build();
    }
}
