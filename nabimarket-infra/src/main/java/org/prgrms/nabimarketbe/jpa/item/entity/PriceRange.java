package org.prgrms.nabimarketbe.jpa.item.entity;

import lombok.Getter;

@Getter
public enum PriceRange {
    PRICE_RANGE_ONE(1, 0, 10_000),
    PRICE_RANGE_TWO(2, 10_000, 50_000),
    PRICE_RANGE_THREE(3, 50_000, 100_000),
    PRICE_RANGE_FOUR(4, 100_000, 200_000),
    PRICE_RANGE_FIVE(5, 200_000, 300_000),
    PRICE_RANGE_SIX(6, 300_000, 400_000),
    PRICE_RANGE_SEVEN(7, 400_000, 500_000),
    PRICE_RANGE_EIGHT(8, 500_000, Integer.MAX_VALUE);

    private Integer value;
    private Integer priceFrom;
    private Integer priceTo;

    PriceRange(
        Integer value,
        Integer priceFrom,
        Integer priceTo
    ) {
        this.value = value;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public boolean isHigherThan(PriceRange priceRange) {
        return this.value > priceRange.value;
    }
}
