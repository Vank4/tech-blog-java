package com.techblog.domain.interaction.repository;

import com.techblog.domain.interaction.model.CompareList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareListRepository extends JpaRepository<CompareList, Long> {

    List<CompareList> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<CompareList> findByUserIdAndName(Long userId, String name);
}