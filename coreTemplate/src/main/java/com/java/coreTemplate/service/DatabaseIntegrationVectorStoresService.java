package com.java.coreTemplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.java.coreTemplate.model.dto.DatabaseIntegrationVectorStores;
import com.java.coreTemplate.repository.DatabaseIntegrationVectorStoresRepository;
import com.java.coreTemplate.exception.ResourceNotFoundException;
import com.java.coreTemplate.exception.ValidationException;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class DatabaseIntegrationVectorStoresService {

    private final DatabaseIntegrationVectorStoresRepository repository;

    @Autowired
    public DatabaseIntegrationVectorStoresService(DatabaseIntegrationVectorStoresRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new or updates an existing DatabaseIntegrationVectorStores entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws ValidationException if the entity is invalid
     */
    @Transactional
    public DatabaseIntegrationVectorStores save(@Valid DatabaseIntegrationVectorStores entity) {
        if (entity.getId() == null) {
            // Optional: Validate required fields before saving
            validateEntity(entity);
        }
        return repository.save(entity);
    }

    /**
     * Finds a DatabaseIntegrationVectorStores entity by its ID.
     *
     * @param id the ID of the entity
     * @return an Optional containing the entity if found, empty otherwise
     */
    public Optional<DatabaseIntegrationVectorStores> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Finds all active (non-deleted, enabled) DatabaseIntegrationVectorStores entities.
     *
     * @return a list of active entities
     */
    public List<DatabaseIntegrationVectorStores> findAllActive() {
        return repository.findByIsActiveTrue();
    }

    /**
     * Finds all DatabaseIntegrationVectorStores entities (including inactive).
     *
     * @return a list of all entities
     */
    public List<DatabaseIntegrationVectorStores> findAll() {
        return repository.findAll();
    }

    /**
     * Deletes a DatabaseIntegrationVectorStores entity by ID.
     *
     * @param id the ID of the entity to delete
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DatabaseIntegrationVectorStores with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Updates an existing entity by ID. Throws exception if not found.
     *
     * @param id the ID of the entity to update
     * @param entity the updated entity data
     * @return the updated entity
     * @throws ResourceNotFoundException if the entity is not found
     * @throws ValidationException if the entity is invalid
     */
    @Transactional
    public DatabaseIntegrationVectorStores update(Long id, @Valid DatabaseIntegrationVectorStores entity) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DatabaseIntegrationVectorStores with id " + id + " not found");
        }

        validateEntity(entity);

        entity.setId(id); // Ensure ID is set for update
        return repository.save(entity);
    }

    /**
     * Validates the entity before saving or updating.
     *
     * @param entity the entity to validate
     * @throws ValidationException if validation fails
     */
    private void validateEntity(DatabaseIntegrationVectorStores entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required and cannot be empty");
        }
        if (entity.getDataSourceType() == null) {
            throw new ValidationException("DataSourceType is required");
        }
        // Add more validations as needed
    }

    /**
     * Checks if an entity with the given name already exists (excluding self if updating).
     *
     * @param name the name to check
     * @param id optional ID to exclude (for update)
     * @return true if a duplicate exists
     */
    public boolean existsByNameAndIdNot(String name, Long id) {
        return repository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    /**
     * Finds by name (case-insensitive).
     *
     * @param name the name to search for
     * @return a list of matching entities
     */
    public List<DatabaseIntegrationVectorStores> findByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }
}