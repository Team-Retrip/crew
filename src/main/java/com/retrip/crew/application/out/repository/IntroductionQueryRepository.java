package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.IntroductionListResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IntroductionQueryRepository {

    Slice<IntroductionListResponse> getIntroductions(UUID crewId, Pageable pageable);

    Long getIntroductionCount(UUID crewId);
}
