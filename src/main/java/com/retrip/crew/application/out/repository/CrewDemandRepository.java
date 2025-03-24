package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.vo.DemandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrewDemandRepository extends JpaRepository<Demand, UUID> {
    Page<Demand> findByCrewIdAndStatus(UUID crewId, DemandStatus pending, Pageable pageRequest);

    Optional<Demand> findByIdAndCrewId(UUID demandId, UUID crewId);
}
