package org.prgrms.nabimarketbe.domain.item.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_range", nullable = false)
    private PriceRange priceRange;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Item(
            String itemName,
            PriceRange priceRange,
            Category category
    ) {
        if (itemName.isBlank()) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        if (priceRange == null || category == null) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        this.itemName = itemName;
        this.priceRange = priceRange;
        this.category = category;
    }
}