package org.prgrms.nabimarketbe.domain.item.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.category.entity.Category;

import javax.persistence.*;

@Entity
@Table(name = "items")
@NoArgsConstructor
@Getter
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;

    @Enumerated(EnumType.STRING)
    private PriceRange priceRange;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Item(
            String itemName,
            PriceRange priceRange,
            Category category
    ) {
        this.itemName = itemName;
        this.priceRange = priceRange;
        this.category = category;
    }
}
