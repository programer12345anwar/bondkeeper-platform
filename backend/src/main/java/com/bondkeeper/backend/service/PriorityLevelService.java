package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.PriorityLevelRequest;
import com.bondkeeper.backend.dto.response.PriorityLevelResponse;

import java.util.List;

public interface PriorityLevelService {

    PriorityLevelResponse create(PriorityLevelRequest request);

    PriorityLevelResponse getById(Long id);

    List<PriorityLevelResponse> getAllForCurrentUser();

    PriorityLevelResponse update(Long id, PriorityLevelRequest request);

    void delete(Long id);
}
