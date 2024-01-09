package org.prgrms.nabimarketbe.item;

import org.prgrms.nabimarketbe.BaseDomain;
import org.prgrms.nabimarketbe.category.Category;

import org.prgrms.nabimarketbe.error.ErrorCode;
import org.prgrms.nabimarketbe.error.BaseException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Item extends BaseDomain {
    private String itemName;

    private PriceRange priceRange;

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
