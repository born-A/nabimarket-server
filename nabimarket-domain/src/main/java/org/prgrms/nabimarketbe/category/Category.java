package org.prgrms.nabimarketbe.category;

import lombok.Getter;
import org.prgrms.nabimarketbe.BasePOJO;

@Getter
public class Category extends BasePOJO {
    private Long categoryId;

    private CategoryEnum categoryName;

    public Category(CategoryEnum categoryName) {
        this.categoryName = categoryName;
    }
}
