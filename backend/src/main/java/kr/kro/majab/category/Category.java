package kr.kro.majab.category;

import jakarta.persistence.*;
import kr.kro.majab.BaseEntity;
import kr.kro.majab.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categories_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<>();

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
