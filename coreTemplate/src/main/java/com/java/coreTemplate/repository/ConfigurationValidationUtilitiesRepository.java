package com.java.coreTemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.coreTemplate.model.dto.ConfigurationValidationUtilities;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationValidationUtilitiesRepository extends JpaRepository<ConfigurationValidationUtilities, Long> {

    /**
     * Finds configuration validation utilities by validation type (e.g., "EMAIL", "PASSWORD").
     * Uses method name derivation for simple queries.
     */
    List<ConfigurationValidationUtilities> findByValidationType(String validationType);

    /**
     * Finds by validation type and active status.
     */
    List<ConfigurationValidationUtilities> findByValidationTypeAndIsActive(String validationType, Boolean isActive);

    /**
     * Finds by validation type and configuration key (e.g., "max_length").
     * Case-insensitive search on configurationKey.
     */
    List<ConfigurationValidationUtilities> findByValidationTypeAndConfigurationKeyIgnoreCase(
            String validationType, String configurationKey);

    /**
     * Finds the latest active configuration by validation type (assuming createdAt timestamp).
     */
    Optional<ConfigurationValidationUtilities> findTopByValidationTypeAndIsActiveOrderByCreatedAtDesc(
            String validationType, Boolean isActive);

    /**
     * Custom query using JPQL to find all active validations with their configuration keys.
     * Uses named parameters for clarity and safety.
     */
    @Query("SELECT c FROM ConfigurationValidationUtilities c " +
           "WHERE c.isActive = true " +
           "AND c.validationType = :validationType " +
           "ORDER BY c.createdAt DESC")
    List<ConfigurationValidationUtilities> findActiveByValidationType(
            @Param("validationType") String validationType);

    /**
     * Custom query to find configurations with a specific regex pattern.
     */
    @Query("SELECT c FROM ConfigurationValidationUtilities c " +
           "WHERE c.validationType = :validationType " +
           "AND c.regexPattern LIKE %:pattern%")
    List<ConfigurationValidationUtilities> findByValidationTypeAndRegexPatternContaining(
            @Param("validationType") String validationType,
            @Param("pattern") String pattern);

    /**
     * Native query to find by configuration key (if you need to use database-specific syntax).
     */
    @Query(value = "SELECT * FROM config_validation_utilities c " +
                   "WHERE c.validation_type = :validationType " +
                   "AND c.configuration_key ILIKE %:key% " +
                   "AND c.is_active = true",
           nativeQuery = true)
    List<ConfigurationValidationUtilities> findActiveByValidationTypeAndConfigurationKeyContaining(
            @Param("validationType") String validationType,
            @Param("key") String key);

    /**
     * Checks if a configuration exists for a given validation type and configuration key.
     */
    boolean existsByValidationTypeAndConfigurationKeyIgnoreCase(String validationType, String configurationKey);

    /**
     * Counts active configurations by validation type.
     */
    long countByValidationTypeAndIsActive(String validationType, Boolean isActive);

    /**
     * Custom query with entity graph to eagerly load related configuration metadata (if any).
     * Example: if ConfigurationValidationUtilities has a @OneToMany or @ManyToOne to another entity.
     */
    @Query("SELECT c FROM ConfigurationValidationUtilities c " +
           "WHERE c.validationType = :validationType")
    @EntityGraph(attributePaths = {"metadata"}) // Assuming 'metadata' is a lazy-loaded relation
    List<ConfigurationValidationUtilities> findWithMetadataByValidationType(
            @Param("validationType") String validationType);
}