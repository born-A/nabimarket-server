package org.prgrms.nabimarketbe.domain.item.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.global.annotation.ValidEnum;
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

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @ValidEnum(enumClass = PriceRange.class, message = "유효하지 않은 가격대입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "price_range", nullable = false)
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
