package com.Zest.product_assesment.Repositories;

import com.Zest.product_assesment.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByProductId(Long productId);
}