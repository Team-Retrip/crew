package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.Introduction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionRepository extends JpaRepository<Introduction, UUID> {

    Optional<Introduction> findByIdAndCrewId(UUID introductionId, UUID crewId);
}
