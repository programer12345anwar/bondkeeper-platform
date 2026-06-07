package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.CategoryRequest;
import com.bondkeeper.backend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAllForCurrentUser();

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
