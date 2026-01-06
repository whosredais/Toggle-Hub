package com.togglehub.service;

import com.togglehub.aspect.Audit;
import com.togglehub.domain.Environment;
import com.togglehub.domain.FeatureFlag;
import com.togglehub.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagService(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    public List<FeatureFlag> findAll() {
        return featureFlagRepository.findAll();
    }

    public List<FeatureFlag> findByApplicationId(Long appId) {
        return featureFlagRepository.findByApplicationId(appId);
    }

    public FeatureFlag findById(Long id) {
        return featureFlagRepository.findById(id).orElseThrow(() -> new RuntimeException("Flag not found"));
    }

    @Transactional
    @Audit(action = "CREATE_FLAG")
    public FeatureFlag createFeatureFlag(FeatureFlag featureFlag) {
        return featureFlagRepository.save(featureFlag);
    }

    @Transactional
    @Audit(action = "UPDATE_FLAG_STATUS")
    public FeatureFlag updateFeatureFlagStatus(Long id, Environment env, boolean enabled) {
        FeatureFlag flag = findById(id);
        flag.getStatuses().put(env, enabled);
        return featureFlagRepository.save(flag);
    }
    
    @Transactional
    @Audit(action = "DELETE_FLAG")
    public void deleteFeatureFlag(Long id) {
        featureFlagRepository.deleteById(id);
    }
}
