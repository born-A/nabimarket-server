package org.prgrms.nabimarketbe.domain.suggestion.entity;

import java.util.function.BiFunction;

import org.prgrms.nabimarketbe.domain.item.entity.Item;

public enum SuggestionType {
    OFFER( (fromItem, toItem) -> (fromItem.getPriceRange().equals(toItem.getPriceRange())) ),
    POKE( (fromItem, toItem) -> (fromItem.getPriceRange().getValue() < toItem.getPriceRange().getValue()) );

    public final BiFunction<Item, Item, Boolean> suggestionAvailableFunction;

    SuggestionType(BiFunction<Item, Item, Boolean> suggestionAvailableFunction) {
        this.suggestionAvailableFunction = suggestionAvailableFunction;
    }

    public boolean isSuggestionAvailable(
        Item fromItem,
        Item toItem
    ) {
        return this.suggestionAvailableFunction.apply(fromItem, toItem);
    }
}
