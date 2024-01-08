package org.prgrms.nabimarketbe.jpa.item.repository;

import org.prgrms.nabimarketbe.jpa.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
