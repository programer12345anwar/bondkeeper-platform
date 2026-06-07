package com.bondkeeper.backend.repository;

import com.bondkeeper.backend.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    List<Interaction> findByContactIdOrderByInteractionDateDesc(Long contactId);

    Optional<Interaction> findByIdAndContactUserId(Long id, Long userId);
}
