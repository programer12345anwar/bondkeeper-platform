package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.ReminderRequest;
import com.bondkeeper.backend.dto.response.ReminderResponse;
import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.Reminder;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.ContactRepository;
import com.bondkeeper.backend.repository.ReminderRepository;
import com.bondkeeper.backend.service.ReminderService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ContactRepository contactRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public ReminderResponse create(ReminderRequest request) {
        Contact contact = findContactForCurrentUser(request.getContactId());
        Reminder reminder = entityMapper.toReminder(request);
        reminder.setContact(contact);
        return entityMapper.toReminderResponse(reminderRepository.save(reminder));
    }

    @Override
    public ReminderResponse getById(Long id) {
        return entityMapper.toReminderResponse(findReminderForCurrentUser(id));
    }

    @Override
    public List<ReminderResponse> getByContactId(Long contactId) {
        findContactForCurrentUser(contactId);
        return reminderRepository.findByContactIdOrderByReminderDateAsc(contactId).stream()
                .map(entityMapper::toReminderResponse)
                .toList();
    }

    @Override
    public List<ReminderResponse> getDueBetween(LocalDate start, LocalDate end) {
        return reminderRepository.findByReminderDateBetweenOrderByReminderDateAsc(start, end).stream()
                .filter(reminder -> reminder.getContact().getUser().getId().equals(SecurityUtils.getCurrentUserId()))
                .map(entityMapper::toReminderResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReminderResponse update(Long id, ReminderRequest request) {
        Reminder reminder = findReminderForCurrentUser(id);
        Contact contact = findContactForCurrentUser(request.getContactId());
        entityMapper.updateReminder(request, reminder);
        reminder.setContact(contact);
        return entityMapper.toReminderResponse(reminderRepository.save(reminder));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = findReminderForCurrentUser(id);
        reminderRepository.delete(reminder);
    }

    private Reminder findReminderForCurrentUser(Long id) {
        return reminderRepository.findByIdAndContactUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));
    }

    private Contact findContactForCurrentUser(Long contactId) {
        return contactRepository.findByIdAndUserId(contactId, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
    }
}
