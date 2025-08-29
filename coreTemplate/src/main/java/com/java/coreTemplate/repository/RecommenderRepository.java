package com.java.coreTemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommenderRepository extends JpaRepository<Recommender, Long> {

    // 1. Find by unique identifier (inherited from JpaRepository)
    // Optional<Recommender> findById(Long id);

    // 2. Find by name (case-insensitive, using method name derivation)
    Optional<Recommender> findByNameIgnoreCase(String name);

    // 3. Find all recommenders by active status
    List<Recommender> findByActiveTrue();

    // 4. Find recommenders by name containing a substring (case-insensitive)
    List<Recommender> findByNameContainingIgnoreCase(String name);

    // 5. Find recommenders by category and active status
    List<Recommender> findByCategoryAndActiveTrue(String category);

    // 6. Find recommenders by multiple criteria using @Query (JPQL)
    @Query("SELECT r FROM Recommender r WHERE r.active = true AND r.priority > :minPriority")
    List<Recommender> findActiveWithPriorityAbove(@Param("minPriority") Integer minPriority);

    // 7. Custom query with native SQL (if needed for performance or complex logic)
    @Query(value = "SELECT * FROM recommenders r WHERE r.name ILIKE %:name% AND r.active = true",
           nativeQuery = true)
    List<Recommender> findActiveByNameContainingIgnoreCaseNative(@Param("name") String name);

    // 8. Find top N recommenders by priority (for ranking use cases)
    @Query("SELECT r FROM Recommender r WHERE r.active = true ORDER BY r.priority DESC")
    List<Recommender> findTopActiveByPriorityDesc(@Param("limit") int limit);

    // 9. Check if a recommender with given name already exists (useful for validation)
    boolean existsByNameIgnoreCase(String name);

    // 10. Count active recommenders by category
    @Query("SELECT COUNT(r) FROM Recommender r WHERE r.active = true AND r.category = :category")
    long countActiveByCategory(@Param("category") String category);

    // 11. Custom method using @Query with pagination (for API endpoints)
    @Query("SELECT r FROM Recommender r WHERE r.active = true")
    List<Recommender> findActiveRecommendersWithPagination(@Param("page") int page, @Param("size") int size);
}