package com.techblog.domain.interaction.repository;

import com.techblog.domain.interaction.model.FeaturedItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeaturedItemRepository extends JpaRepository<FeaturedItem, Long> {

    List<FeaturedItem> findByActiveTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByPriorityDesc(
            LocalDateTime start,
            LocalDateTime end);
}
