package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.CrewListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CrewQueryRepository {
    Slice<CrewListResponse> getCrews(Pageable pageable, String keyword);
    Long getCrewCount(String keyword);
}
