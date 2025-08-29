package com.java.coreTemplate.model.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "database integration & vector stores")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseIntegrationVectorStores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "connection_string", nullable = false, length = 500)
    private String connectionString;

    @Column(name = "vector_store_type", nullable = false, length = 50)
    private String vectorStoreType;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;

    @Version
    private Long version;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "is_encrypted")
    private boolean isEncrypted;

    @Column(name = "last_synced_at")
    private java.time.LocalDateTime lastSyncedAt;

    @Column(name = "status_message", length = 255)
    private String statusMessage;

    @Column(name = "environment", length = 50)
    private String environment;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    // Optional fields with proper handling
    private Optional<String> apiKey = Optional.empty();

    private Optional<String> credentials = Optional.empty();

    // Additional validation constraints can be added via Bean Validation annotations
    // Example: @NotBlank, @Email, @Size, etc. (if needed)
}