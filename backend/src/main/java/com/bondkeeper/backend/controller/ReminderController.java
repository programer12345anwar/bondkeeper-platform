package com.bondkeeper.backend.controller;

import com.bondkeeper.backend.dto.request.ReminderRequest;
import com.bondkeeper.backend.dto.response.ApiResponse;
import com.bondkeeper.backend.dto.response.ReminderResponse;
import com.bondkeeper.backend.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/reminders")
@RequiredArgsConstructor
@Tag(name = "Reminders", description = "Contact reminder management")
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    @Operation(summary = "Create a reminder")
    public ResponseEntity<ApiResponse<ReminderResponse>> create(@Valid @RequestBody ReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reminder created", reminderService.create(request)));
    }

    @GetMapping("/due")
    @Operation(summary = "List reminders due within a date range")
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> getDueBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success(reminderService.getDueBetween(start, end)));
    }

    @GetMapping("/contact/{contactId}")
    @Operation(summary = "List reminders for a contact")
    public ResponseEntity<ApiResponse<List<ReminderResponse>>> getByContactId(@PathVariable Long contactId) {
        return ResponseEntity.ok(ApiResponse.success(reminderService.getByContactId(contactId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reminder by ID")
    public ResponseEntity<ApiResponse<ReminderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reminderService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reminder")
    public ResponseEntity<ApiResponse<ReminderResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ReminderRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Reminder updated", reminderService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reminder")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reminderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Reminder deleted", null));
    }
}
