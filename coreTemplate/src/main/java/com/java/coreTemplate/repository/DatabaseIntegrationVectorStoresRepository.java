package com.java.coreTemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DatabaseIntegrationVectorStoresRepository extends JpaRepository<DatabaseIntegrationVectorStores, Long> {

    /**
     * Find a vector store by its unique name (e.g., collection name or dataset name).
     * Uses method name derivation for clean, readable queries.
     */
    Optional<DatabaseIntegrationVectorStores> findByName(String name);

    /**
     * Find all vector stores by their associated database integration ID.
     */
    List<DatabaseIntegrationVectorStores> findByDatabaseIntegrationId(Long databaseIntegrationId);

    /**
     * Find all active vector stores (assuming there's an 'isActive' boolean field).
     */
    List<DatabaseIntegrationVectorStores> findByIsActiveTrue();

    /**
     * Find vector stores by name (case-insensitive) using JPQL.
     */
    @Query("SELECT v FROM DatabaseIntegrationVectorStores v WHERE LOWER(v.name) = LOWER(:name)")
    Optional<DatabaseIntegrationVectorStores> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find vector stores by name or description (partial match, case-insensitive).
     */
    @Query("SELECT v FROM DatabaseIntegrationVectorStores v " +
           "WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "   OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<DatabaseIntegrationVectorStores> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    /**
     * Count total number of vector stores for a given database integration.
     */
    long countByDatabaseIntegrationId(Long databaseIntegrationId);

    /**
     * Check if a vector store with the given name already exists for a specific integration.
     */
    boolean existsByNameAndDatabaseIntegrationId(String name, Long databaseIntegrationId);

    /**
     * Delete by name and integration ID (useful for cleanup).
     */
    void deleteByNameAndDatabaseIntegrationId(String name, Long databaseIntegrationId);
}