package com.techblog.domain.product.repository;

import com.techblog.domain.product.model.UseCase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseCaseRepository extends JpaRepository<UseCase, Long> {

    Optional<UseCase> findByName(String name);
}
