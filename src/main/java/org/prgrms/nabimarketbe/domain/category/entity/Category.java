package org.prgrms.nabimarketbe.domain.category.entity;

import lombok.Getter;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryName;
}
