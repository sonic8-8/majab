package kr.kro.majab.store;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
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
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stores_id")
    private Long id;

    private String name;

    private String ownerName;
    private String businessName;
    private String businessAddress;
    private String businessNumber;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
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
    public Store(String name, String ownerName, String businessName, String businessAddress, String businessNumber, String phoneNumber, StoreStatus storeStatus, LocalDateTime pickupStartTime, LocalDateTime pickupEndTime, LocalDateTime suspendedStartTime, LocalDateTime suspendedEndTime, LocalDateTime openTime, LocalDateTime closeTime) {
        this.name = name;
        this.ownerName = ownerName;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
        this.storeStatus = storeStatus;
        this.pickupStartTime = pickupStartTime;
        this.pickupEndTime = pickupEndTime;
        this.suspendedStartTime = suspendedStartTime;
        this.suspendedEndTime = suspendedEndTime;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public void changeOwner(Owner owner) {
        this.owner = owner;
        owner.getStores().add(this);
    }

    public void changeCategory(Category category) {
        this.category = category;
        category.getStores().add(this);
    }

    public void addNotice(Notice notice) {
        notices.add(notice);
        notice.changeStore(this);
    }

    public void addFollow(Follow follow) {
        follows.add(follow);
        follow.changeStore(this);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.changeStore(this);
    }

    public void addItem(Item item) {
        items.add(item);
        item.changeStore(this);
    }
}
