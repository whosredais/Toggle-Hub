package com.togglehub.web.api;

import com.togglehub.domain.Application;
import com.togglehub.domain.Environment;
import com.togglehub.domain.FeatureFlag;
import com.togglehub.service.ApplicationService;
import com.togglehub.service.FeatureFlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flags")
public class FeatureFlagApiController {

    private final FeatureFlagService featureFlagService;
    private final ApplicationService applicationService;

    public FeatureFlagApiController(FeatureFlagService featureFlagService, ApplicationService applicationService) {
        this.featureFlagService = featureFlagService;
        this.applicationService = applicationService;
    }

    @GetMapping("/{appName}")
    public ResponseEntity<Map<String, Boolean>> getFlags(
            @PathVariable String appName,
            @RequestParam(defaultValue = "PRODUCTION") Environment env,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {

        // Basic Security Check (Improvement available: Move to SecurityFilter)
        Application app = applicationService.findByName(appName)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (apiKey != null && !app.getApiKey().equals(apiKey)) {
             return ResponseEntity.status(403).build();
        }

        List<FeatureFlag> flags = featureFlagService.findByApplicationId(app.getId());

        Map<String, Boolean> flagMap = flags.stream()
                .collect(Collectors.toMap(
                        FeatureFlag::getName,
                        f -> f.isEnabled(env)
                ));

        return ResponseEntity.ok(flagMap);
    }
}
