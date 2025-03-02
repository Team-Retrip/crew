package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.response.CrewListResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import org.springframework.data.domain.Pageable;

public interface GetCrewUseCase {
    ScrollPageResponse<CrewListResponse> getCrews(Pageable pageable, String keyword, CrewOrder order, String sort);
}
