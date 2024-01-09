package org.prgrms.nabimarketbe.category;

import lombok.Getter;
import org.prgrms.nabimarketbe.BaseDomain;

@Getter
public class Category extends BaseDomain {
    private Long categoryId;

    private CategoryEnum categoryName;

    public Category(CategoryEnum categoryName) {
        this.categoryName = categoryName;
    }
}
