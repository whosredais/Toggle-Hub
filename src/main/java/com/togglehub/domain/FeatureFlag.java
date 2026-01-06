package com.togglehub.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Entity
@Table(name = "feature_flags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "promoNoel"

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "feature_flag_status", joinColumns = @JoinColumn(name = "feature_flag_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "environment")
    @Column(name = "is_enabled")
    private Map<Environment, Boolean> statuses;

    public boolean isEnabled(Environment env) {
        if (statuses == null) {
            return false;
        }
        return statuses.getOrDefault(env, false);
    }
}
