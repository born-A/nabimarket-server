package org.prgrms.nabimarketbe.domain.category.repository;

import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByCategoryName(CategoryEnum categoryEnum);
}
