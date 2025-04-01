package kr.kro.majab.user;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.order.Order;
import kr.kro.majab.review.Review;
import lombok.AccessLevel;
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
    private List<Order> orders = new ArrayList<>();

    public User(String email, String password, String loginType, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

}
