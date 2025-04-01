package kr.kro.majab.store;

import jakarta.persistence.*;
import kr.kro.majab.category.Category;
import kr.kro.majab.follow.Follow;
import kr.kro.majab.item.Item;
import kr.kro.majab.notice.Notice;
import kr.kro.majab.order.Order;
import kr.kro.majab.owner.Owner;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stores_id")
    private Long id;

    private String name = "가게명을 입력하세요";

    private String ownerName = "대표자명";
    private String businessName = "상호명";
    private String businessAddress = "사업자 주소";
    private String businessNumber = "사업자등록번호";
    private String phoneNumber = "전화번호";

    private StoreStatus storeStatus = StoreStatus.CLOSE;

    private LocalDateTime pickupStartTime = LocalDateTime.now();
    private LocalDateTime pickupEndTime = LocalDateTime.now();

    private LocalDateTime suspendedStartTime = LocalDateTime.now();
    private LocalDateTime suspendedEndTime = LocalDateTime.now();

    private LocalDateTime openTime = LocalDateTime.now();
    private LocalDateTime closeTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owners_id")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id")
    private Category category;

    @OneToMany(mappedBy = "store")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    @Builder
    public Store(Owner owner) {
        this.owner = owner;
        this.items.add(new Item(this));
    }
}
