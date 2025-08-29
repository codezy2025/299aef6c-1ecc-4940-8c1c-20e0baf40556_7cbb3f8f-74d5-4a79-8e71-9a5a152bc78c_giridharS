package com.java.coreTemplate.service;

import com.java.coreTemplate.exception.ResourceNotFoundException;
import com.java.coreTemplate.model.dto.Userfeedbackmodule;
import com.java.coreTemplate.repository.UserfeedbackmoduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserfeedbackmoduleService {

    private final UserfeedbackmoduleRepository repository;

    @Autowired
    public UserfeedbackmoduleService(UserfeedbackmoduleRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new or updates an existing Userfeedbackmodule entity.
     *
     * @param entity the Userfeedbackmodule to save
     * @return the saved entity
     * @throws IllegalArgumentException if entity is null
     */
    @Transactional
    public Userfeedbackmodule save(Userfeedbackmodule entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Userfeedbackmodule entity cannot be null");
        }
        return repository.save(entity);
    }

    /**
     * Finds a Userfeedbackmodule by its ID.
     *
     * @param id the ID of the entity
     * @return Optional containing the entity if found, empty otherwise
     * @throws IllegalArgumentException if id is null
     */
    public Optional<Userfeedbackmodule> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        return repository.findById(id);
    }

    /**
     * Retrieves all active Userfeedbackmodule entities (where isActive = true).
     *
     * @return list of active Userfeedbackmodule entities
     */
    public List<Userfeedbackmodule> findAllActive() {
        return repository.findByIsActiveTrue();
    }

    /**
     * Retrieves all Userfeedbackmodule entities regardless of status.
     *
     * @return list of all Userfeedbackmodule entities
     */
    public List<Userfeedbackmodule> findAll() {
        return repository.findAll();
    }

    /**
     * Updates an existing Userfeedbackmodule entity.
     *
     * @param id     the ID of the entity to update
     * @param entity the updated entity data
     * @return the updated entity
     * @throws ResourceNotFoundException if the entity with given ID is not found
     */
    @Transactional
    public Userfeedbackmodule update(Long id, Userfeedbackmodule entity) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Updated entity must not be null");
        }

        return repository.findById(id)
                .map(existingEntity -> {
                    // Update fields as needed (example: assuming you have setters)
                    existingEntity.setFeedbackText(entity.getFeedbackText());
                    existingEntity.setRating(entity.getRating());
                    existingEntity.setIsActive(entity.getIsActive());
                    // Add other fields as necessary
                    return repository.save(existingEntity);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Userfeedbackmodule with id " + id + " not found"));
    }

    /**
     * Deletes a Userfeedbackmodule entity by ID.
     *
     * @param id the ID of the entity to delete
     * @throws ResourceNotFoundException if the entity with given ID is not found
     */
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Userfeedbackmodule with id " + id + " not found");
        }

        repository.deleteById(id);
    }

    /**
     * Checks if a Userfeedbackmodule with the given ID exists.
     *
     * @param id the ID to check
     * @return true if exists, false otherwise
     */
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Finds all Userfeedbackmodule entities by feedback text (case-insensitive partial match).
     *
     * @param feedbackText the text to search for
     * @return list of matching entities
     */
    public List<Userfeedbackmodule> findByFeedbackTextContainingIgnoreCase(String feedbackText) {
        if (feedbackText == null || feedbackText.trim().isEmpty()) {
            return List.of(); // Return empty list instead of null
        }
        return repository.findByFeedbackTextContainingIgnoreCase(feedbackText.trim());
    }

    /**
     * Finds all Userfeedbackmodule entities with a specific rating.
     *
     * @param rating the rating to filter by
     * @return list of entities with matching rating
     */
    public List<Userfeedbackmodule> findByRating(int rating) {
        return repository.findByRating(rating);
    }
}