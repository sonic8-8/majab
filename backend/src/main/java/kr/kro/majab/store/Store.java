package kr.kro.majab.store;

import jakarta.persistence.*;
import kr.kro.majab.category.Category;
import kr.kro.majab.follow.Follow;
import kr.kro.majab.item.Item;
import kr.kro.majab.notice.Notice;
import kr.kro.majab.order.Order;
import kr.kro.majab.owner.Owner;
import lombok.AccessLevel;
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

    private String name;

    private String ownerName;
    private String businessName;
    private String businessAddress;
    private String businessNumber;
    private String phoneNumber;

    private StoreStatus storeStatus;

    private LocalDateTime pickupStartTime;
    private LocalDateTime pickupEndTime;

    private LocalDateTime suspendedStartTime;
    private LocalDateTime suspendedEndTime;

    private LocalDateTime openTime;
    private LocalDateTime closeTime;

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
}
