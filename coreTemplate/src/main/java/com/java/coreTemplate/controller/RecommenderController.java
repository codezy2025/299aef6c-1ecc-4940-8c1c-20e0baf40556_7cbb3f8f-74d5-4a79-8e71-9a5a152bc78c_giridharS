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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.java.coreTemplate.model.dto.Recommender;
import com.java.coreTemplate.service.RecommenderService;

import java.util.Optional;

@Tag(name = "Recommender API", description = "Operations for managing recommenders")
@RestController
@RequestMapping("/api/v1/recommender")
@Validated
public class RecommenderController {

    private final RecommenderService service;

    public RecommenderController(RecommenderService service) {
        this.service = service;
    }

    @Operation(
        summary = "Create a new recommender",
        description = "Saves a new recommender entity with provided data",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Recommender data to be created",
            required = true,
            content = @Content(schema = @Schema(implementation = Recommender.class))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Recommender created successfully",
                content = @Content(schema = @Schema(implementation = Recommender.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping
    public ResponseEntity<Recommender> create(@Validated @RequestBody Recommender entity) {
        Recommender saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
        summary = "Get recommender by ID",
        description = "Fetches a recommender by its unique ID",
        parameters = {
            @Parameter(name = "id", description = "The ID of the recommender", required = true, example = "1")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Recommender found",
                content = @Content(schema = @Schema(implementation = Recommender.class))),
            @ApiResponse(responseCode = "404", description = "Recommender not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Recommender> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get all recommenders with pagination",
        description = "Returns a paginated list of all recommenders with optional sorting",
        parameters = {
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Number of items per page", example = "10"),
            @Parameter(name = "sort", description = "Sort field (e.g., 'name,asc' or 'createdAt,desc')", example = "createdAt,desc")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "List of recommenders retrieved successfully",
                content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping
    public ResponseEntity<Page<Recommender>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Recommender> recommenders = service.findAll(pageable);
        return ResponseEntity.ok(recommenders);
    }

    @Operation(
        summary = "Update an existing recommender",
        description = "Updates a recommender by ID. If the entity does not exist, returns 404.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Recommender updated successfully",
                content = @Content(schema = @Schema(implementation = Recommender.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or ID mismatch"),
            @ApiResponse(responseCode = "404", description = "Recommender not found")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Recommender> update(@PathVariable Long id, @Validated @RequestBody Recommender entity) {
        if (!id.equals(entity.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return service.findById(id)
                .map(existing -> ResponseEntity.ok(service.save(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Delete a recommender by ID",
        description = "Deletes a recommender by its ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Recommender deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recommender not found")
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
}