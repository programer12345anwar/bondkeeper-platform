package com.bondkeeper.backend.repository;

import com.bondkeeper.backend.entity.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriorityLevelRepository extends JpaRepository<PriorityLevel, Long> {

    List<PriorityLevel> findByUserIdOrderByLevelNameAsc(Long userId);

    Optional<PriorityLevel> findByIdAndUserId(Long id, Long userId);

    boolean existsByLevelNameAndUserId(String levelName, Long userId);
}
