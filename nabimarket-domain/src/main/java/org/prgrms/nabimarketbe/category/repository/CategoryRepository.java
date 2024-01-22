package org.prgrms.nabimarketbe.category.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByCategoryName(CategoryEnum categoryEnum);
}
