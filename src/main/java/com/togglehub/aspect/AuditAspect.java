package com.togglehub.aspect;

import com.togglehub.domain.FeatureFlag;
import com.togglehub.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @AfterReturning(pointcut = "@annotation(audit)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Audit audit, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = (auth != null) ? auth.getName() : "SYSTEM";
        String action = audit.action();
        String target = "Unknown";
        String details = "";

        if (result instanceof FeatureFlag) {
            FeatureFlag flag = (FeatureFlag) result;
            target = "Flag: " + flag.getName();
            details = "Status: " + flag.getStatuses();
        } else if (joinPoint.getArgs().length > 0) {
            // Fallback to first arg if present (e.g. for delete)
             target = "Arg: " + joinPoint.getArgs()[0].toString();
        }

        auditService.log(actor, action, target, details);
    }
}
