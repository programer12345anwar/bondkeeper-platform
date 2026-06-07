package com.bondkeeper.backend.controller;

import com.bondkeeper.backend.dto.request.PriorityLevelRequest;
import com.bondkeeper.backend.dto.response.ApiResponse;
import com.bondkeeper.backend.dto.response.PriorityLevelResponse;
import com.bondkeeper.backend.service.PriorityLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/priority-levels")
@RequiredArgsConstructor
@Tag(name = "Priority Levels", description = "Contact priority and reminder frequency settings")
@SecurityRequirement(name = "bearerAuth")
public class PriorityLevelController {

    private final PriorityLevelService priorityLevelService;

    @PostMapping
    @Operation(summary = "Create a priority level")
    public ResponseEntity<ApiResponse<PriorityLevelResponse>> create(
            @Valid @RequestBody PriorityLevelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Priority level created", priorityLevelService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List priority levels for current user")
    public ResponseEntity<ApiResponse<List<PriorityLevelResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(priorityLevelService.getAllForCurrentUser()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get priority level by ID")
    public ResponseEntity<ApiResponse<PriorityLevelResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(priorityLevelService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update priority level")
    public ResponseEntity<ApiResponse<PriorityLevelResponse>> update(
            @PathVariable Long id, @Valid @RequestBody PriorityLevelRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Priority level updated",
                priorityLevelService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete priority level")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        priorityLevelService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Priority level deleted", null));
    }
}
