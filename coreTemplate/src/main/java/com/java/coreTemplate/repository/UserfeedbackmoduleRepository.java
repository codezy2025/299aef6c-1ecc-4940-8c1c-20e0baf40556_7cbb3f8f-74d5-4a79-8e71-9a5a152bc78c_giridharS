package com.java.coreTemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserfeedbackmoduleRepository extends JpaRepository<Userfeedbackmodule, Long> {

    /**
     * Find feedback by user ID (assuming Userfeedbackmodule has a userId field)
     */
    List<Userfeedbackmodule> findByUserId(Long userId);

    /**
     * Find feedback by user ID and feedback status (e.g., "PENDING", "RESOLVED")
     */
    List<Userfeedbackmodule> findByUserIdAndStatus(Long userId, String status);

    /**
     * Find feedback by feedback type (e.g., "BUG", "SUGGESTION") and created after a specific date
     */
    List<Userfeedbackmodule> findByFeedbackTypeAndCreatedDateAfter(String feedbackType, java.time.LocalDateTime date);

    /**
     * Find feedback by user ID, status, and order by creation date descending
     */
    List<Userfeedbackmodule> findByUserIdAndStatusOrderByCreatedDateDesc(Long userId, String status);

    /**
     * Custom query using JPQL to find feedback with pagination and filtering
     */
    @Query("SELECT f FROM Userfeedbackmodule f " +
           "WHERE f.status = :status " +
           "AND f.createdDate >= :startDate " +
           "ORDER BY f.createdDate DESC")
    List<Userfeedbackmodule> findActiveFeedbackByStatusAndDate(
            @Param("status") String status,
            @Param("startDate") java.time.LocalDateTime startDate);

    /**
     * Count total feedback entries for a given user
     */
    @Query("SELECT COUNT(f) FROM Userfeedbackmodule f WHERE f.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Find feedback by ID and user ID (for security context checks)
     */
    Optional<Userfeedbackmodule> findByIdAndUserId(Long id, Long userId);

    /**
     * Check if feedback exists for a given user and feedback ID
     */
    boolean existsByIdAndUserId(Long id, Long userId);
}