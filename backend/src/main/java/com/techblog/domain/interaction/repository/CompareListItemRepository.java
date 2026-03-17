package com.techblog.domain.interaction.repository;

import com.techblog.domain.interaction.model.CompareListItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareListItemRepository extends JpaRepository<CompareListItem, Long> {

    List<CompareListItem> findByCompareListIdOrderByPositionAsc(Long compareListId);
}
