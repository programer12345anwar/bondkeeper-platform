package com.bondkeeper.backend.controller;

import com.bondkeeper.backend.dto.request.InteractionRequest;
import com.bondkeeper.backend.dto.response.ApiResponse;
import com.bondkeeper.backend.dto.response.InteractionResponse;
import com.bondkeeper.backend.service.InteractionService;
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
import java.util.Map;

@RestController
@RequestMapping("/v1/interactions")
@RequiredArgsConstructor
@Tag(name = "Interactions", description = "Contact interaction history")
@SecurityRequirement(name = "bearerAuth")
public class InteractionController {

    private final InteractionService interactionService;

    @PostMapping
    @Operation(summary = "Log a new interaction and recalculate relationship score")
    public ResponseEntity<ApiResponse<InteractionResponse>> create(
            @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Interaction logged", interactionService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get interaction by ID")
    public ResponseEntity<ApiResponse<InteractionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(interactionService.getById(id)));
    }

    @GetMapping("/contact/{contactId}")
    @Operation(summary = "List interactions for a contact")
    public ResponseEntity<ApiResponse<List<InteractionResponse>>> getByContactId(@PathVariable Long contactId) {
        return ResponseEntity.ok(ApiResponse.success(interactionService.getByContactId(contactId)));
    }

    @PostMapping("/contact/{contactId}/recalculate-score")
    @Operation(summary = "Recalculate relationship score for a contact")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> recalculateScore(@PathVariable Long contactId) {
        Integer score = interactionService.recalculateRelationshipScore(contactId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("relationshipScore", score)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update interaction")
    public ResponseEntity<ApiResponse<InteractionResponse>> update(
            @PathVariable Long id, @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Interaction updated", interactionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete interaction")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        interactionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Interaction deleted", null));
    }
}
