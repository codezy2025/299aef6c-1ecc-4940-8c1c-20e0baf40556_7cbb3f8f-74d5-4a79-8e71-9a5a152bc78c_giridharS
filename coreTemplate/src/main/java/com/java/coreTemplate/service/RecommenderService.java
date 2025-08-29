package com.java.coreTemplate.service;

import com.java.coreTemplate.model.dto.Recommender;
import com.java.coreTemplate.repository.RecommenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RecommenderService {

    private final RecommenderRepository repository;

    // Constructor injection (preferred over @Autowired on fields)
    @Autowired
    public RecommenderService(RecommenderRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new recommender entity.
     *
     * @param entity the recommender to save
     * @return the saved entity
     */
    @Transactional(readOnly = false)
    public Recommender save(Recommender entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Recommender entity cannot be null");
        }
        return repository.save(entity);
    }

    /**
     * Finds a recommender by its ID.
     *
     * @param id the ID of the recommender
     * @return Optional containing the recommender if found, empty otherwise
     */
    public Optional<Recommender> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        return repository.findById(id);
    }

    /**
     * Retrieves all active recommenders (where isActive = true).
     *
     * @return list of active recommenders
     */
    public List<Recommender> findAllActive() {
        return repository.findByIsActiveTrue();
    }

    /**
     * Finds all recommenders associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return list of recommenders for the given user
     */
    public List<Recommender> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        return repository.findByUserId(userId);
    }

    /**
     * Finds recommenders by their status (e.g., PENDING, APPROVED).
     *
     * @param status the status to filter by
     * @return list of recommenders with the given status
     */
    public List<Recommender> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status must not be null or empty");
        }
        return repository.findByStatus(status);
    }

    /**
     * Updates an existing recommender entity.
     *
     * @param id the ID of the recommender to update
     * @param updatedEntity the updated recommender data
     * @return the updated recommender, or empty if not found
     */
    @Transactional(readOnly = false)
    public Optional<Recommender> update(Long id, Recommender updatedEntity) {
        if (id == null || updatedEntity == null) {
            throw new IllegalArgumentException("ID and entity must not be null");
        }

        return findById(id)
                .map(existing -> {
                    // Update fields as needed
                    existing.setSomeField(updatedEntity.getSomeField()); // Replace with actual fields
                    existing.setStatus(updatedEntity.getStatus());
                    existing.setIsActive(updatedEntity.getIsActive());
                    // ... other fields

                    return repository.save(existing);
                });
    }

    /**
     * Deletes a recommender by ID.
     *
     * @param id the ID of the recommender to delete
     * @return true if deleted, false otherwise
     */
    @Transactional(readOnly = false)
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }

        if (findById(id).isEmpty()) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    /**
     * Checks if a recommender exists by ID.
     *
     * @param id the ID to check
     * @return true if exists, false otherwise
     */
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}