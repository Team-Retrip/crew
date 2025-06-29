package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;

import java.util.UUID;

public interface ManageRecruitmentQuestionUseCase {
    CreateRecruitmentQuestionResponse createRecruitmentQuestion(UUID crewId, CreateRecruitmentQuestionRequest request);
    UpdateRecruitmentQuestionResponse updateRecruitmentQuestion(UUID crewId, UUID questionId, UpdateRecruitmentQuestionRequest request);
    void deleteRecruitmentQuestion(UUID crewId, UUID questionId, UUID memberId);
}
