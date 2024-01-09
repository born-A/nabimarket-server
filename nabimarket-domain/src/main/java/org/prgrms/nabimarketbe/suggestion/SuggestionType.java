package org.prgrms.nabimarketbe.suggestion;

import lombok.Getter;
import org.prgrms.nabimarketbe.item.Item;

import java.util.function.BiFunction;

@Getter
public enum SuggestionType {
    OFFER("오퍼", (fromItem, toItem) -> (fromItem.getPriceRange().equals(toItem.getPriceRange())
        || fromItem.getPriceRange().getValue() > toItem.getPriceRange().getValue())),
    POKE("찔러보기", (fromItem, toItem) -> (fromItem.getPriceRange().getValue() < toItem.getPriceRange().getValue()));

    private final String name;

    public final BiFunction<Item, Item, Boolean> suggestionValidationFunction;

    SuggestionType(
        String name,
        BiFunction<Item, Item, Boolean> suggestionValidationFunction
    ) {
        this.name = name;
        this.suggestionValidationFunction = suggestionValidationFunction;
    }

    public boolean isSuggestionAvailable(
        Item fromItem,
        Item toItem
    ) {
        return this.suggestionValidationFunction.apply(fromItem, toItem);
    }
}
