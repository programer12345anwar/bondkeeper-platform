package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.ReminderRequest;
import com.bondkeeper.backend.dto.response.ReminderResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReminderService {

    ReminderResponse create(ReminderRequest request);

    ReminderResponse getById(Long id);

    List<ReminderResponse> getByContactId(Long contactId);

    List<ReminderResponse> getDueBetween(LocalDate start, LocalDate end);

    ReminderResponse update(Long id, ReminderRequest request);

    void delete(Long id);
}
