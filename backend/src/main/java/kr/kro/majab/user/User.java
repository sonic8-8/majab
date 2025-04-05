package kr.kro.majab.user;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.address.Address;
import kr.kro.majab.follow.Follow;
import kr.kro.majab.order.Order;
import kr.kro.majab.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    private String email;

    private String password;

    private String loginType;

    private String nickname;

    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();

    @Builder
    public User(String email, String password, String loginType, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.changeUser(this);
    }

    public void addFollow(Follow follow) {
        follows.add(follow);
        follow.changeUser(this);
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.changeUser(this);
    }
}
