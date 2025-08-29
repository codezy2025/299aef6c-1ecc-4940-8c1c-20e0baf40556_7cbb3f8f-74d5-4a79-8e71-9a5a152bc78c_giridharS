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

import com.java.coreTemplate.model.dto.Userfeedbackmodule;
import com.java.coreTemplate.service.UserfeedbackmoduleService;

import java.util.Optional;

@Tag(name = "User Feedback Module API", description = "Operations related to user feedback management")
@RestController
@RequestMapping("/api/v1/user-feedback-module")
@Validated
public class UserfeedbackmoduleController {

    private final UserfeedbackmoduleService service;

    public UserfeedbackmoduleController(UserfeedbackmoduleService service) {
        this.service = service;
    }

    @Operation(
        summary = "Create a new user feedback",
        description = "Saves a new user feedback entity. Returns the created entity with HTTP 201 status.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User feedback data to be created",
            required = true,
            content = @Content(schema = @Schema(implementation = Userfeedbackmodule.class))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Feedback created successfully", content = @Content(schema = @Schema(implementation = Userfeedbackmodule.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping
    public ResponseEntity<Userfeedbackmodule> create(@Validated @RequestBody Userfeedbackmodule entity) {
        Userfeedbackmodule saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
        summary = "Get user feedback by ID",
        description = "Retrieves a user feedback by its unique ID.",
        parameters = {
            @Parameter(name = "id", description = "The unique identifier of the feedback", required = true, example = "123")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Feedback found", content = @Content(schema = @Schema(implementation = Userfeedbackmodule.class))),
            @ApiResponse(responseCode = "404", description = "Feedback not found", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Userfeedbackmodule> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get all user feedbacks with pagination",
        description = "Returns a paginated list of all user feedbacks. Supports sorting and filtering.",
        parameters = {
            @Parameter(name = "page", description = "Page number (0-based)", example = "0", required = false),
            @Parameter(name = "size", description = "Number of items per page", example = "10", required = false),
            @Parameter(name = "sort", description = "Sort field (e.g., 'createdAt,desc')", example = "createdAt,desc", required = false)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "List of feedbacks retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping
    public ResponseEntity<Page<Userfeedbackmodule>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Userfeedbackmodule> feedbacks = service.findAll(pageable);
        return ResponseEntity.ok(feedbacks);
    }

    @Operation(
        summary = "Search user feedbacks by criteria",
        description = "Searches for feedbacks using optional filters like feedbackType, status, or keyword.",
        parameters = {
            @Parameter(name = "feedbackType", description = "Filter by feedback type", example = "BUG_REPORT"),
            @Parameter(name = "status", description = "Filter by status", example = "PENDING"),
            @Parameter(name = "keyword", description = "Search in content or title", example = "login error"),
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Number of items per page", example = "10"),
            @Parameter(name = "sort", description = "Sort field (e.g., 'createdAt,desc')", example = "createdAt,desc")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Filtered list of feedbacks retrieved", content = @Content(schema = @Schema(implementation = Page.class)))
        }
    )
    @GetMapping("/search")
    public ResponseEntity<Page<Userfeedbackmodule>> search(
            @RequestParam(required = false) String feedbackType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Userfeedbackmodule> results = service.search(feedbackType, status, keyword, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(
        summary = "Update existing user feedback",
        description = "Updates a user feedback by ID. Returns updated entity with HTTP 200.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Feedback updated successfully", content = @Content(schema = @Schema(implementation = Userfeedbackmodule.class))),
            @ApiResponse(responseCode = "404", description = "Feedback not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Userfeedbackmodule> update(@PathVariable Long id, @Validated @RequestBody Userfeedbackmodule entity) {
        return service.findById(id)
                .map(existing -> {
                    entity.setId(id); // Ensure ID is preserved
                    Userfeedbackmodule updated = service.save(entity);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Delete user feedback by ID",
        description = "Deletes a user feedback by its ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Feedback deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}