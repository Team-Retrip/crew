package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.CrewListResponse;
import com.retrip.crew.domain.entity.Crew;

import java.util.Optional;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CrewQueryRepository {
    Slice<CrewListResponse> getCrews(Pageable pageable, String keyword);
    Long getCrewCount(String keyword);
    Optional<Crew> findByIdWithPosts(UUID id);
}
