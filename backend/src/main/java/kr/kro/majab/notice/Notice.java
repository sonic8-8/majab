package kr.kro.majab.notice;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.owner.Owner;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Lazy;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notices_id")
    private Long id;

    private String title;

    private String content;

    private String pictureUrl;

    private LocalDateTime registeredDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stores_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owners_id")
    private Owner owner;

    @Builder
    public Notice(String title, String content, String pictureUrl, LocalDateTime registeredDateTime) {
        this.title = title;
        this.content = content;
        this.pictureUrl = pictureUrl;
        this.registeredDateTime = registeredDateTime;
    }

    public void changeStore(Store store) {
        this.store = store;
        store.getNotices().add(this);
    }

    public void changeOwner(Owner owner) {
        this.owner = owner;
        owner.getNotices().add(this);
    }
}
