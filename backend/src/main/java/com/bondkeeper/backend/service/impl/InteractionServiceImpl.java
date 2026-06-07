package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.InteractionRequest;
import com.bondkeeper.backend.dto.response.InteractionResponse;
import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.Interaction;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.ContactRepository;
import com.bondkeeper.backend.repository.InteractionRepository;
import com.bondkeeper.backend.service.InteractionService;
import com.bondkeeper.backend.util.RelationshipScoreCalculator;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;
    private final ContactRepository contactRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public InteractionResponse create(InteractionRequest request) {
        Contact contact = findContactForCurrentUser(request.getContactId());
        Interaction interaction = entityMapper.toInteraction(request);
        interaction.setContact(contact);
        updateContactLastInteractionDate(contact, request.getInteractionDate());
        Interaction saved = interactionRepository.save(interaction);
        int score = recalculateAndSaveRelationshipScore(contact);
        return toResponseWithScore(saved, score);
    }

    @Override
    public InteractionResponse getById(Long id) {
        return entityMapper.toInteractionResponse(findInteractionForCurrentUser(id));
    }

    @Override
    public List<InteractionResponse> getByContactId(Long contactId) {
        findContactForCurrentUser(contactId);
        return interactionRepository.findByContactIdOrderByInteractionDateDesc(contactId).stream()
                .map(entityMapper::toInteractionResponse)
                .toList();
    }

    @Override
    @Transactional
    public InteractionResponse update(Long id, InteractionRequest request) {
        Interaction interaction = findInteractionForCurrentUser(id);
        Contact contact = findContactForCurrentUser(request.getContactId());
        entityMapper.updateInteraction(request, interaction);
        interaction.setContact(contact);
        updateContactLastInteractionDate(contact, request.getInteractionDate());
        Interaction saved = interactionRepository.save(interaction);
        int score = recalculateAndSaveRelationshipScore(contact);
        return toResponseWithScore(saved, score);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Interaction interaction = findInteractionForCurrentUser(id);
        Contact contact = interaction.getContact();
        interactionRepository.delete(interaction);
        recalculateAndSaveRelationshipScore(contact);
    }

    @Override
    @Transactional
    public Integer recalculateRelationshipScore(Long contactId) {
        Contact contact = findContactForCurrentUser(contactId);
        return recalculateAndSaveRelationshipScore(contact);
    }

    private InteractionResponse toResponseWithScore(Interaction interaction, int score) {
        InteractionResponse response = entityMapper.toInteractionResponse(interaction);
        response.setUpdatedRelationshipScore(score);
        return response;
    }

    private int recalculateAndSaveRelationshipScore(Contact contact) {
        List<Interaction> interactions = interactionRepository
                .findByContactIdOrderByInteractionDateDesc(contact.getId());
        int score = RelationshipScoreCalculator.calculate(contact, interactions);
        contact.setRelationshipScore(score);
        contactRepository.save(contact);
        return score;
    }

    private Interaction findInteractionForCurrentUser(Long id) {
        return interactionRepository.findByIdAndContactUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found with id: " + id));
    }

    private Contact findContactForCurrentUser(Long contactId) {
        return contactRepository.findByIdAndUserId(contactId, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
    }

    private void updateContactLastInteractionDate(Contact contact, java.time.LocalDate interactionDate) {
        if (contact.getLastInteractionDate() == null
                || interactionDate.isAfter(contact.getLastInteractionDate())) {
            contact.setLastInteractionDate(interactionDate);
            contactRepository.save(contact);
        }
    }
}
