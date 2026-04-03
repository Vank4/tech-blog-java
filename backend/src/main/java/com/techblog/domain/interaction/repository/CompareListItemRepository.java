package com.techblog.domain.interaction.repository;

import com.techblog.domain.interaction.model.CompareListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompareListItemRepository extends JpaRepository<CompareListItem, Long> {

    List<CompareListItem> findByCompareListIdOrderByPositionAsc(Long compareListId);

    Optional<CompareListItem> findByCompareListIdAndProductId(Long compareListId, Long productId);

    @Query("select coalesce(max(item.position), 0) from CompareListItem item where item.compareList.id = :compareListId")
    int findMaxPositionByCompareListId(@Param("compareListId") Long compareListId);
}