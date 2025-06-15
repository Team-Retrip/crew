package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.domain.entity.RecruitmentQuestion;

import java.util.List;
import java.util.UUID;

public interface RecruitmentQuestionQueryRepository {
    List<RecruitmentQuestionResponse> findRecruitmentQuestions(UUID crewId);
    RecruitmentQuestion findByIdOrElseThrow(UUID questionId);
}
