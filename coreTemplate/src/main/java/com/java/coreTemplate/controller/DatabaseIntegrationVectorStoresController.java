package com.java.coreTemplate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.coreTemplate.model.dto.DatabaseIntegrationVectorStores;
import com.java.coreTemplate.service.DatabaseIntegrationVectorStoresService;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Database Integration & Vector Stores", description = "API for managing database integration and vector store configurations")
@RestController
@RequestMapping("/api/v1/database-integration-vector-stores")
public class DatabaseIntegrationVectorStoresController {

    private final DatabaseIntegrationVectorStoresService service;

    public DatabaseIntegrationVectorStoresController(DatabaseIntegrationVectorStoresService service) {
        this.service = service;
    }

    @Operation(
        summary = "Create a new Database Integration & Vector Store",
        description = "Saves a new integration configuration with validation.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The database integration and vector store details",
            required = true,
            content = @Content(schema = @Schema(implementation = DatabaseIntegrationVectorStores.class))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Integration created successfully", content = @Content(schema = @Schema(implementation = DatabaseIntegrationVectorStores.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DatabaseIntegrationVectorStores> create(@Valid @RequestBody DatabaseIntegrationVectorStores entity) {
        DatabaseIntegrationVectorStores saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
        summary = "Get a Database Integration & Vector Store by ID",
        description = "Retrieves a specific integration configuration by its unique ID.",
        parameters = {
            @Parameter(name = "id", description = "The unique identifier of the integration", required = true, example = "123")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Integration found", content = @Content(schema = @Schema(implementation = DatabaseIntegrationVectorStores.class))),
            @ApiResponse(responseCode = "404", description = "Integration not found", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DatabaseIntegrationVectorStores> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get all Database Integration & Vector Stores with pagination",
        description = "Retrieves a paginated list of all integration configurations with optional sorting.",
        parameters = {
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Number of items per page", example = "10"),
            @Parameter(name = "sort", description = "Field to sort by (e.g., name, createdAt)", example = "createdAt,desc")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "List of integrations retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping
    public ResponseEntity<Page<DatabaseIntegrationVectorStores>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<DatabaseIntegrationVectorStores> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Update an existing Database Integration & Vector Store",
        description = "Updates the configuration of an existing integration by ID.",
        parameters = {
            @Parameter(name = "id", description = "The unique identifier of the integration to update", required = true, example = "123")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Integration updated successfully", content = @Content(schema = @Schema(implementation = DatabaseIntegrationVectorStores.class))),
            @ApiResponse(responseCode = "404", description = "Integration not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DatabaseIntegrationVectorStores> update(
            @PathVariable Long id,
            @Valid @RequestBody DatabaseIntegrationVectorStores entity) {
        return service.findById(id)
                .map(existing -> {
                    entity.setId(id); // Ensure ID is preserved
                    DatabaseIntegrationVectorStores updated = service.save(entity);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Delete a Database Integration & Vector Store",
        description = "Deletes an integration configuration by its ID.",
        parameters = {
            @Parameter(name = "id", description = "The unique identifier of the integration to delete", required = true, example = "123")
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Integration deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Integration not found")
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Search Database Integration & Vector Stores by name or type",
        description = "Performs a fuzzy search on integration name or type using partial match.",
        parameters = {
            @Parameter(name = "query", description = "Search term (e.g., name or type)", required = true, example = "PostgreSQL")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Matching integrations found", content = @Content(schema = @Schema(implementation = List.class)))
        }
    )
    @GetMapping("/search")
    public ResponseEntity<List<DatabaseIntegrationVectorStores>> search(
            @RequestParam String query,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        List<DatabaseIntegrationVectorStores> results = service.searchByQuery(query, pageable);
        return ResponseEntity.ok(results);
    }
}