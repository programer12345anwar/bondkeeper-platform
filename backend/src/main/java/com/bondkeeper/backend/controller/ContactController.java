package com.bondkeeper.backend.controller;

import com.bondkeeper.backend.dto.request.ContactRequest;
import com.bondkeeper.backend.dto.response.ApiResponse;
import com.bondkeeper.backend.dto.response.ContactResponse;
import com.bondkeeper.backend.dto.response.PageResponse;
import com.bondkeeper.backend.service.ContactService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/contacts")
@RequiredArgsConstructor
@Tag(name = "Contacts", description = "Relationship contact management")
@SecurityRequirement(name = "bearerAuth")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Create a contact")
    public ResponseEntity<ApiResponse<ContactResponse>> create(@Valid @RequestBody ContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contact created", contactService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List all contacts for current user")
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(contactService.getAllForCurrentUser()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search contacts with pagination and filters")
    public ResponseEntity<ApiResponse<PageResponse<ContactResponse>>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long priorityLevelId,
            @RequestParam(required = false) Boolean innerCircle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PageResponse<ContactResponse> result = contactService.search(
                query, categoryId, priorityLevelId, innerCircle, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/inner-circle")
    @Operation(summary = "List inner circle contacts")
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getInnerCircle() {
        return ResponseEntity.ok(ApiResponse.success(contactService.getInnerCircleForCurrentUser()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact by ID")
    public ResponseEntity<ApiResponse<ContactResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(contactService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contact")
    public ResponseEntity<ApiResponse<ContactResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ContactRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Contact updated", contactService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Contact deleted", null));
    }
}
