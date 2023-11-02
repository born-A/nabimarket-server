package org.prgrms.nabimarketbe.domain.item.entity;

public enum PriceRange {
    PRICE_RANGE_ONE(0, 10_000),
    PRICE_RANGE_TWO(10_000, 50_000),
    PRICE_RANGE_THREE(50_000, 100_000),
    PRICE_RANGE_FOUR(100_000, 200_000),
    PRICE_RANGE_FIVE( 200_000, 300_000),
    PRICE_RANGE_SIX(300_000, 400_000),
    PRICE_RANGE_SEVEN(400_000, 500_000),
    PRICE_RANGE_EIGHT(500_000, Integer.MAX_VALUE);

    private Integer priceFrom;
    private Integer priceTo;

    PriceRange(Integer priceFrom, Integer priceTo) {
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }
}
