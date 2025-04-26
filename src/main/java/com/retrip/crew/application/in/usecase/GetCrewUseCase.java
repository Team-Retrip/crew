package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface GetCrewUseCase {
    ScrollPageResponse<CrewListResponse> getCrews(Pageable pageable, String keyword, CrewOrder order, String sort);

    CrewDetailResponse getCrewDetail(UUID crewId);
}
