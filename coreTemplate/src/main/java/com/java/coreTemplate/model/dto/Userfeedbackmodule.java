package com.java.coreTemplate.model.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user feedback module")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Userfeedbackmodule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "feedback_date", nullable = false)
    private java.time.LocalDateTime feedbackDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_resolved")
    private boolean isResolved;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;

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

    public boolean isResolved() {
        return isResolved;
    }
}