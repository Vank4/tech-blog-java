package com.techblog.domain.interaction.repository;

import com.techblog.domain.interaction.model.CompareList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareListRepository extends JpaRepository<CompareList, Long> {

    List<CompareList> findByUserIdOrderByCreatedAtDesc(Long userId);
}
