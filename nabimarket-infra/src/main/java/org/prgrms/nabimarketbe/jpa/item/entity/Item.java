package org.prgrms.nabimarketbe.jpa.item.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.prgrms.nabimarketbe.jpa.BaseEntity;
import org.prgrms.nabimarketbe.jpa.category.entity.Category;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Item(
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

    public void updateItem(
        String itemName,
        PriceRange priceRange,
        Category category
    ) {
        this.itemName = itemName;
        this.priceRange = priceRange;
        this.category = category;
    }
}
