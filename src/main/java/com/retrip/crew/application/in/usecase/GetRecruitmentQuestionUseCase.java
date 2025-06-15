package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import java.util.List;
import java.util.UUID;

public interface GetRecruitmentQuestionUseCase {
    List<RecruitmentQuestionResponse> getRecruitmentQuestions(UUID crewId);
}