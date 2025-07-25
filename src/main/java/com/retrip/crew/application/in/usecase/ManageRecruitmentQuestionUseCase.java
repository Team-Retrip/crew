package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import java.util.List;
import java.util.UUID;

public interface ManageRecruitmentQuestionUseCase {
    CreateRecruitmentQuestionResponse createRecruitmentQuestion(UUID memberId, UUID crewId, CreateRecruitmentQuestionRequest request);

    UpdateRecruitmentQuestionResponse updateRecruitmentQuestion(UUID memberId, UUID crewId, UUID questionId, UpdateRecruitmentQuestionRequest request);

    void deleteRecruitmentQuestion(UUID memberId, UUID crewId, UUID questionId);

    List<RecruitmentQuestionResponse> getRecruitmentQuestions(UUID crewId, UUID memberId);
}
