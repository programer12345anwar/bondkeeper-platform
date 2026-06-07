package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.ContactRequest;
import com.bondkeeper.backend.dto.response.ContactResponse;
import com.bondkeeper.backend.dto.response.PageResponse;

import java.util.List;

public interface ContactService {

    ContactResponse create(ContactRequest request);

    ContactResponse getById(Long id);

    List<ContactResponse> getAllForCurrentUser();

    PageResponse<ContactResponse> search(
            String query,
            Long categoryId,
            Long priorityLevelId,
            Boolean innerCircle,
            int page,
            int size,
            String sortBy,
            String sortDirection);

    List<ContactResponse> getInnerCircleForCurrentUser();

    ContactResponse update(Long id, ContactRequest request);

    void delete(Long id);
}
