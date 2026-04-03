package com.techblog.domain.ai.repository;

import com.techblog.domain.ai.model.MlModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MlModelRepository extends JpaRepository<MlModel, Long> {

    Optional<MlModel> findByNameAndVersion(String name, String version);

    Optional<MlModel> findByActiveTrue();
}
