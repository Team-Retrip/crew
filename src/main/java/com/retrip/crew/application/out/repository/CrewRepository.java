package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CrewRepository extends JpaRepository<Crew, UUID> {
}
