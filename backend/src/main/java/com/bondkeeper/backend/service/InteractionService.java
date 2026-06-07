package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.InteractionRequest;
import com.bondkeeper.backend.dto.response.InteractionResponse;

import java.util.List;

public interface InteractionService {

    InteractionResponse create(InteractionRequest request);

    InteractionResponse getById(Long id);

    List<InteractionResponse> getByContactId(Long contactId);

    InteractionResponse update(Long id, InteractionRequest request);

    void delete(Long id);

    Integer recalculateRelationshipScore(Long contactId);
}
