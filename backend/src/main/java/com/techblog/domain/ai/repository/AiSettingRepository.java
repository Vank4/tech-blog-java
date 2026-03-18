package com.techblog.domain.ai.repository;

import com.techblog.domain.ai.model.AiSetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiSettingRepository extends JpaRepository<AiSetting, Long> {

    Optional<AiSetting> findByActiveTrue();
}
