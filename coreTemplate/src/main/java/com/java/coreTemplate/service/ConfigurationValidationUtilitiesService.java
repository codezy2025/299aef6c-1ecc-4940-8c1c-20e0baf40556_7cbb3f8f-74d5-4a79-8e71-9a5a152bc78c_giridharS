package com.java.coreTemplate.service;

import com.java.coreTemplate.model.dto.ConfigurationValidationUtilities;
import com.java.coreTemplate.repository.ConfigurationValidationUtilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing ConfigurationValidationUtilities entities.
 * Provides business logic and transactional operations for configuration validation data.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationValidationUtilitiesService {

    private final ConfigurationValidationUtilitiesRepository repository;

    /**
     * Constructor injection for dependency injection.
     *
     * @param repository the repository to interact with the database
     */
    @Autowired
    public ConfigurationValidationUtilitiesService(ConfigurationValidationUtilitiesRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new or updates an existing ConfigurationValidationUtilities entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws IllegalArgumentException if entity is null
     */
    @Transactional
    public ConfigurationValidationUtilities save(ConfigurationValidationUtilities entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return repository.save(entity);
    }

    /**
     * Finds a ConfigurationValidationUtilities entity by its ID.
     *
     * @param id the ID of the entity
     * @return an Optional containing the entity if found, otherwise empty
     */
    public Optional<ConfigurationValidationUtilities> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Retrieves all active (is_active = true) configuration validation utilities.
     *
     * @return a list of active configuration validation utilities
     */
    public List<ConfigurationValidationUtilities> findAllActive() {
        return repository.findByIsActiveTrue();
    }

    /**
     * Retrieves all configuration validation utilities, regardless of activation status.
     *
     * @return a list of all configuration validation utilities
     */
    public List<ConfigurationValidationUtilities> findAll() {
        return repository.findAll();
    }

    /**
     * Checks if a configuration validation utility with the given ID exists.
     *
     * @param id the ID to check
     * @return true if exists, false otherwise
     */
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Deletes a configuration validation utility by ID.
     *
     * @param id the ID of the entity to delete
     * @throws IllegalArgumentException if id is null
     */
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        repository.deleteById(id);
    }

    /**
     * Updates an existing configuration validation utility.
     * If the entity does not exist, throws an exception.
     *
     * @param entity the updated entity
     * @return the updated entity
     * @throws IllegalArgumentException if entity is null or does not exist
     */
    @Transactional
    public ConfigurationValidationUtilities update(ConfigurationValidationUtilities entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (!repository.existsById(entity.getId())) {
            throw new IllegalArgumentException("Entity with ID " + entity.getId() + " does not exist");
        }
        return repository.save(entity);
    }

    /**
     * Finds a configuration validation utility by its unique name.
     *
     * @param name the name of the utility
     * @return an Optional containing the entity if found, otherwise empty
     */
    public Optional<ConfigurationValidationUtilities> findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * Finds all configuration validation utilities by type.
     *
     * @param type the type of utility
     * @return a list of configuration validation utilities matching the type
     */
    public List<ConfigurationValidationUtilities> findByType(String type) {
        return repository.findByType(type);
    }
}