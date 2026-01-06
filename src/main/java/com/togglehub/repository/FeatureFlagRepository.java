package com.togglehub.repository;

import com.togglehub.domain.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    List<FeatureFlag> findByApplicationId(Long applicationId);
    Optional<FeatureFlag> findByApplicationIdAndName(Long applicationId, String name);
}
