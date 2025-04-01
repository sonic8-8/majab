package kr.kro.majab.address;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addresses_id")
    private Long id;

    private String nickname;

    private String address; //todo: 지도 api 사용할 경우 변경

    private boolean isDefault;

    public Address(String nickname, String address, boolean isDefault) {
        this.nickname = nickname;
        this.address = address;
        this.isDefault = isDefault;
    }
}
