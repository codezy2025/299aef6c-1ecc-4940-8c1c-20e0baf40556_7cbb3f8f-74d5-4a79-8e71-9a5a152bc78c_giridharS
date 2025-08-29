package com.java.coreTemplate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.coreTemplate.model.dto.ConfigurationValidationUtilities;
import com.java.coreTemplate.service.ConfigurationValidationUtilitiesService;

import java.util.List;

@Tag(name = "Configuration Validation Utilities", description = "API for managing configuration validation utilities")
@RestController
@RequestMapping("/api/v1/configuration-validation-utilities") // Fixed: replaced space with hyphen
public class ConfigurationValidationUtilitiesController {

    private final ConfigurationValidationUtilitiesService service;

    public ConfigurationValidationUtilitiesController(ConfigurationValidationUtilitiesService service) {
        this.service = service;
    }

    @Operation(
        summary = "Create a new configuration validation utility",
        description = "Saves a new configuration validation utility entity.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Configuration validation utility to create",
            required = true,
            content = @Content(schema = @Schema(implementation = ConfigurationValidationUtilities.class))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Configuration validation utility created successfully",
                content = @Content(schema = @Schema(implementation = ConfigurationValidationUtilities.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping
    public ResponseEntity<ConfigurationValidationUtilities> create(
            @Valid @RequestBody ConfigurationValidationUtilities entity) {
        ConfigurationValidationUtilities saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
        summary = "Get configuration validation utility by ID",
        description = "Retrieves a configuration validation utility by its unique ID.",
        parameters = {
            @Parameter(name = "id", description = "Unique identifier of the configuration validation utility", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuration validation utility found",
                content = @Content(schema = @Schema(implementation = ConfigurationValidationUtilities.class))),
            @ApiResponse(responseCode = "404", description = "Configuration validation utility not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ConfigurationValidationUtilities> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get all configuration validation utilities with pagination",
        description = "Returns a paginated list of all configuration validation utilities.",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of configuration validation utilities",
                content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping
    public ResponseEntity<Page<ConfigurationValidationUtilities>> getAll(
            @Parameter(description = "Page number (0-based), default is 0")
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<ConfigurationValidationUtilities> page = service.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
        summary = "Update an existing configuration validation utility",
        description = "Updates the configuration validation utility with the provided ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuration validation utility updated successfully",
                content = @Content(schema = @Schema(implementation = ConfigurationValidationUtilities.class))),
            @ApiResponse(responseCode = "404", description = "Configuration validation utility not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ConfigurationValidationUtilities> update(
            @PathVariable Long id,
            @Valid @RequestBody ConfigurationValidationUtilities entity) {
        return service.findById(id)
                .map(existing -> {
                    entity.setId(id); // Ensure ID is preserved
                    ConfigurationValidationUtilities updated = service.save(entity);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Delete a configuration validation utility",
        description = "Deletes a configuration validation utility by its ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Configuration validation utility deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration validation utility not found")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Search configuration validation utilities by name or description",
        description = "Performs a fuzzy search across name and description fields.",
        parameters = {
            @Parameter(name = "query", description = "Search term for name or description", required = true),
            @Parameter(name = "page", description = "Page number (0-based), default is 0"),
            @Parameter(name = "size", description = "Page size, default is 10")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Search results",
                content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ConfigurationValidationUtilities>> search(
            @RequestParam String query,
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<ConfigurationValidationUtilities> results = service.searchByKeyword(query, pageable);
        return ResponseEntity.ok(results);
    }
}