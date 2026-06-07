package com.bondkeeper.backend.repository;

import com.bondkeeper.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {

    List<Contact> findByUserIdOrderByNameAsc(Long userId);

    List<Contact> findByUserIdAndInnerCircleTrueOrderByNameAsc(Long userId);

    Optional<Contact> findByIdAndUserId(Long id, Long userId);
}
