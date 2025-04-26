package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.IntroductionCreateRequest;
import com.retrip.crew.application.in.request.IntroductionDeleteRequest;
import com.retrip.crew.application.in.request.IntroductionUpdateRequest;
import com.retrip.crew.application.in.response.IntroductionCreateResponse;
import com.retrip.crew.application.in.response.IntroductionDetailResponse;
import com.retrip.crew.application.in.response.IntroductionListResponse;
import com.retrip.crew.application.in.response.IntroductionUpdateResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ManageIntroductionUseCase {
    IntroductionCreateResponse createIntroduction(UUID crewId, IntroductionCreateRequest request);

    IntroductionUpdateResponse updateIntroduction(UUID crewId, UUID introductionId, IntroductionUpdateRequest request);

    void deleteIntroduction(UUID crewId, UUID introductionId, IntroductionDeleteRequest request);

    IntroductionDetailResponse getIntroduction(UUID crewId, UUID introductionId);

    ScrollPageResponse<IntroductionListResponse> getIntroductions(UUID crewId, Pageable pageable);
}
