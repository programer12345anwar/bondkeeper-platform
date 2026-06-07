package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.PriorityLevelRequest;
import com.bondkeeper.backend.dto.response.PriorityLevelResponse;
import com.bondkeeper.backend.entity.PriorityLevel;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.exception.DuplicateResourceException;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.PriorityLevelRepository;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.service.PriorityLevelService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriorityLevelServiceImpl implements PriorityLevelService {

    private final PriorityLevelRepository priorityLevelRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public PriorityLevelResponse create(PriorityLevelRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (priorityLevelRepository.existsByLevelNameAndUserId(request.getLevelName(), userId)) {
            throw new DuplicateResourceException("Priority level already exists: " + request.getLevelName());
        }
        User user = findUserOrThrow(userId);
        PriorityLevel priorityLevel = entityMapper.toPriorityLevel(request);
        priorityLevel.setUser(user);
        return entityMapper.toPriorityLevelResponse(priorityLevelRepository.save(priorityLevel));
    }

    @Override
    public PriorityLevelResponse getById(Long id) {
        return entityMapper.toPriorityLevelResponse(findPriorityLevelForCurrentUser(id));
    }

    @Override
    public List<PriorityLevelResponse> getAllForCurrentUser() {
        return priorityLevelRepository.findByUserIdOrderByLevelNameAsc(SecurityUtils.getCurrentUserId()).stream()
                .map(entityMapper::toPriorityLevelResponse)
                .toList();
    }

    @Override
    @Transactional
    public PriorityLevelResponse update(Long id, PriorityLevelRequest request) {
        PriorityLevel priorityLevel = findPriorityLevelForCurrentUser(id);
        entityMapper.updatePriorityLevel(request, priorityLevel);
        return entityMapper.toPriorityLevelResponse(priorityLevelRepository.save(priorityLevel));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PriorityLevel priorityLevel = findPriorityLevelForCurrentUser(id);
        priorityLevelRepository.delete(priorityLevel);
    }

    private PriorityLevel findPriorityLevelForCurrentUser(Long id) {
        return priorityLevelRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Priority level not found with id: " + id));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
