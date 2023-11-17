package org.prgrms.nabimarketbe.domain.suggestion.entity;

import java.util.function.BiFunction;

import org.prgrms.nabimarketbe.domain.item.entity.Item;

public enum SuggestionType {
    OFFER( (fromItem, toItem) -> (fromItem.getPriceRange().equals(toItem.getPriceRange())) ),
    POKE( (fromItem, toItem) -> (fromItem.getPriceRange().getValue() < toItem.getPriceRange().getValue()
        || fromItem.getPriceRange().getValue() > toItem.getPriceRange().getValue()));

    public final BiFunction<Item, Item, Boolean> suggestionValidationFunction;

    SuggestionType(BiFunction<Item, Item, Boolean> suggestionAvailableFunction) {
        this.suggestionValidationFunction = suggestionAvailableFunction;
    }

    public boolean isSuggestionAvailable(
        Item fromItem,
        Item toItem
    ) {
        return this.suggestionValidationFunction.apply(fromItem, toItem);
    }
}
