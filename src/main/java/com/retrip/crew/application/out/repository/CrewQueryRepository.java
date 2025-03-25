package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.demand.CrewsOfDemandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface CrewQueryRepository {
    Slice<CrewListResponse> getCrews(Pageable pageable, String keyword);
    Long getCrewCount(String keyword);

    Page<CrewsOfDemandResponse> findAllContainsMember(Pageable pageable, UUID memberId);
}
