package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.CategoryRequest;
import com.bondkeeper.backend.dto.response.CategoryResponse;
import com.bondkeeper.backend.entity.Category;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.exception.DuplicateResourceException;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.CategoryRepository;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.service.CategoryService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (categoryRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }
        User user = findUserOrThrow(userId);
        Category category = entityMapper.toCategory(request);
        category.setUser(user);
        return entityMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getById(Long id) {
        return entityMapper.toCategoryResponse(findCategoryForCurrentUser(id));
    }

    @Override
    public List<CategoryResponse> getAllForCurrentUser() {
        return categoryRepository.findByUserIdOrderByNameAsc(SecurityUtils.getCurrentUserId()).stream()
                .map(entityMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategoryForCurrentUser(id);
        entityMapper.updateCategory(request, category);
        return entityMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findCategoryForCurrentUser(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryForCurrentUser(Long id) {
        return categoryRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
