package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.response.ChangeRecruitmentStatusResponse;

import java.util.UUID;

public interface UpdateRecruitmentUseCase {
    ChangeRecruitmentStatusResponse startRecruitment(UUID crewId);

    ChangeRecruitmentStatusResponse stopRecruitment(UUID crewId);
}
