package org.prgrms.nabimarketbe.domain.category.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private CategoryEnum categoryName;

    public Category(CategoryEnum categoryName) {
        this.categoryName = categoryName;
    }
}
