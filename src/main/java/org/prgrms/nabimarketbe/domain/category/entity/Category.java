package org.prgrms.nabimarketbe.domain.category.entity;

import lombok.Getter;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.global.annotation.ValidEnum;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ValidEnum(enumClass = CategoryEnum.class, message = "유효하지 않은 카테고리입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private CategoryEnum categoryName;
}
