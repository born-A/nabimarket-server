package org.prgrms.nabimarketbe.domain.category.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false, unique = true)
    private CategoryEnum categoryName;

    public Category(CategoryEnum categoryName) {
        this.categoryName = categoryName;
    }
}
