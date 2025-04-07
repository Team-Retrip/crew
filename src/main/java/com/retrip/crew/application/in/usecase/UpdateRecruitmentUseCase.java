package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.response.demand.ChangeRecruitmentStatusResponse;

import java.util.UUID;

public interface UpdateRecruitmentUseCase {
    ChangeRecruitmentStatusResponse startRecruitment(UUID crewId);

    ChangeRecruitmentStatusResponse stopRecruitment(UUID crewId);
}
