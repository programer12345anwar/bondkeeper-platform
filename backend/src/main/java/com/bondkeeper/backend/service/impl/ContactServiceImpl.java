package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.ContactRequest;
import com.bondkeeper.backend.dto.response.ContactResponse;
import com.bondkeeper.backend.dto.response.PageResponse;
import com.bondkeeper.backend.entity.Category;
import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.PriorityLevel;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.CategoryRepository;
import com.bondkeeper.backend.repository.ContactRepository;
import com.bondkeeper.backend.repository.ContactSpecification;
import com.bondkeeper.backend.repository.PriorityLevelRepository;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.service.ContactService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name", "relationshipScore", "lastInteractionDate", "createdAt");

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PriorityLevelRepository priorityLevelRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public ContactResponse create(ContactRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = findUserOrThrow(userId);
        Contact contact = entityMapper.toContact(request);
        contact.setUser(user);
        applyOptionalRelations(contact, request, userId);
        applyDefaults(contact, request);
        return entityMapper.toContactResponse(contactRepository.save(contact));
    }

    @Override
    public ContactResponse getById(Long id) {
        return entityMapper.toContactResponse(findContactForCurrentUser(id));
    }

    @Override
    public List<ContactResponse> getAllForCurrentUser() {
        return contactRepository.findByUserIdOrderByNameAsc(SecurityUtils.getCurrentUserId()).stream()
                .map(entityMapper::toContactResponse)
                .toList();
    }

    @Override
    public PageResponse<ContactResponse> search(
            String query,
            Long categoryId,
            Long priorityLevelId,
            Boolean innerCircle,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Long userId = SecurityUtils.getCurrentUserId();
        Specification<Contact> spec = ContactSpecification.withFilters(
                userId, query, categoryId, priorityLevelId, innerCircle);

        String resolvedSort = ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "name";
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, resolvedSort));

        Page<ContactResponse> result = contactRepository.findAll(spec, pageable)
                .map(entityMapper::toContactResponse);

        return PageResponse.from(result);
    }

    @Override
    public List<ContactResponse> getInnerCircleForCurrentUser() {
        return contactRepository.findByUserIdAndInnerCircleTrueOrderByNameAsc(SecurityUtils.getCurrentUserId()).stream()
                .map(entityMapper::toContactResponse)
                .toList();
    }

    @Override
    @Transactional
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = findContactForCurrentUser(id);
        entityMapper.updateContact(request, contact);
        applyOptionalRelations(contact, request, SecurityUtils.getCurrentUserId());
        applyDefaults(contact, request);
        return entityMapper.toContactResponse(contactRepository.save(contact));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Contact contact = findContactForCurrentUser(id);
        contactRepository.delete(contact);
    }

    private Contact findContactForCurrentUser(Long id) {
        return contactRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private void applyOptionalRelations(Contact contact, ContactRequest request, Long userId) {
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));
            contact.setCategory(category);
        }
        if (request.getPriorityLevelId() != null) {
            PriorityLevel priorityLevel = priorityLevelRepository.findByIdAndUserId(
                            request.getPriorityLevelId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Priority level not found with id: " + request.getPriorityLevelId()));
            contact.setPriorityLevel(priorityLevel);
        }
    }

    private void applyDefaults(Contact contact, ContactRequest request) {
        if (request.getRelationshipScore() == null) {
            contact.setRelationshipScore(contact.getRelationshipScore() != null ? contact.getRelationshipScore() : 50);
        }
        if (request.getInnerCircle() == null && contact.getInnerCircle() == null) {
            contact.setInnerCircle(false);
        }
    }
}
