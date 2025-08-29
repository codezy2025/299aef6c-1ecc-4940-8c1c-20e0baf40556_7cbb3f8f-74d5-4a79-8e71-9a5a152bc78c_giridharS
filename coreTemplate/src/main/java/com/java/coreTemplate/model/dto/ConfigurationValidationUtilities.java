package com.java.coreTemplate.model.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuration & validation utilities")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationValidationUtilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "validation_rule", columnDefinition = "TEXT")
    private String validationRule;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Boolean getter with 'is' prefix as required
    public boolean isIsActive() {
        return isActive;
    }

    // Optional fields for nullable data
    @Column(name = "max_retries", nullable = true)
    private Integer maxRetries;

    @Column(name = "timeout_seconds", nullable = true)
    private Integer timeoutSeconds;

    @Column(name = "enabled_for_production", nullable = false)
    private boolean enabledForProduction;

    // Boolean getter with 'is' prefix
    public boolean isEnabledForProduction() {
        return enabledForProduction;
    }

    // Optional field for dynamic configuration
    @Column(name = "dynamic_config", columnDefinition = "JSON")
    private String dynamicConfig;

    // Optional wrapper for configuration data
    public Optional<String> getDynamicConfig() {
        return Optional.ofNullable(dynamicConfig);
    }

    // Optional wrapper for maxRetries
    public Optional<Integer> getMaxRetries() {
        return Optional.ofNullable(maxRetries);
    }

    // Optional wrapper for timeoutSeconds
    public Optional<Integer> getTimeoutSeconds() {
        return Optional.ofNullable(timeoutSeconds);
    }
}