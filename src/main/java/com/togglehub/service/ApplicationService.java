package com.togglehub.service;

import com.togglehub.domain.Application;
import com.togglehub.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Transactional
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }
    
    public Optional<Application> findByName(String name) {
        return applicationRepository.findByName(name);
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }
    
    public Optional<Application> findByApiKey(String apiKey) {
        return applicationRepository.findByApiKey(apiKey);
    }
}
