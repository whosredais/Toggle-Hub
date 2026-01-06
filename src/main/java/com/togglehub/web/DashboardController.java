package com.togglehub.web;

import com.togglehub.domain.Application;
import com.togglehub.domain.Environment;
import com.togglehub.domain.FeatureFlag;
import com.togglehub.service.ApplicationService;
import com.togglehub.service.AuditService;
import com.togglehub.service.FeatureFlagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private final ApplicationService applicationService;
    private final FeatureFlagService featureFlagService;
    private final AuditService auditService;

    public DashboardController(ApplicationService applicationService, FeatureFlagService featureFlagService, AuditService auditService) {
        this.applicationService = applicationService;
        this.featureFlagService = featureFlagService;
        this.auditService = auditService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("applications", applicationService.findAll());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/applications")
    public String createApplication(@ModelAttribute Application application) {
        applicationService.createApplication(application);
        return "redirect:/";
    }

    @GetMapping("/applications/{id}")
    public String viewApplication(@PathVariable Long id, Model model) {
        Application app = applicationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        System.out.println("DEBUG: Viewing App ID: " + app.getId());
        
        model.addAttribute("application", app);
        model.addAttribute("applicationId", app.getId()); // Explicitly add ID
        model.addAttribute("flags", featureFlagService.findByApplicationId(id));
        model.addAttribute("environments", Environment.values());
        return "application";
    }

    @PostMapping("/flags")
    public String createFlag(
            @RequestParam(required = false) String applicationId, // String to handle empty/null
            @RequestParam String name, 
            @RequestParam(required = false) String description) {
        
        System.out.println("DEBUG: createFlag - Raw appId: '" + applicationId + "'");
        
        if (applicationId == null || applicationId.trim().isEmpty()) {
             throw new IllegalArgumentException("Application ID is missing");
        }
        
        Long appIdParsed = Long.parseLong(applicationId);

        Application app = applicationService.findById(appIdParsed)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + appIdParsed));
        
        FeatureFlag featureFlag = FeatureFlag.builder()
                .name(name)
                .description(description)
                .application(app)
                .statuses(new java.util.HashMap<>())
                .build();
                
        featureFlagService.createFeatureFlag(featureFlag);
        return "redirect:/applications/" + applicationId;
    }

    @PostMapping("/flags/{id}/toggle")
    public String toggleFlag(@PathVariable Long id, @RequestParam Environment env, @RequestParam(defaultValue = "false") boolean enabled) {
        FeatureFlag flag = featureFlagService.updateFeatureFlagStatus(id, env, enabled);
        return "redirect:/applications/" + flag.getApplication().getId();
    }
    
    @PostMapping("/flags/{id}/delete")
    public String deleteFlag(@PathVariable Long id) {
        FeatureFlag flag = featureFlagService.findById(id);
        Long appId = flag.getApplication().getId();
        featureFlagService.deleteFeatureFlag(id);
        return "redirect:/applications/" + appId;
    }

    @GetMapping("/audit")
    public String auditLog(Model model) {
        model.addAttribute("logs", auditService.getAllLogs());
        return "audit";
    }
}
