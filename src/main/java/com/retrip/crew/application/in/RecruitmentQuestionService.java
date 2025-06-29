package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.usecase.GetRecruitmentQuestionUseCase;
import com.retrip.crew.application.in.usecase.ManageRecruitmentQuestionUseCase;
import com.retrip.crew.application.out.repository.CrewMemberQueryRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.RecruitmentQuestionQueryRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import com.retrip.crew.domain.entity.RecruitmentQuestion;
import com.retrip.crew.domain.exception.CrewMemberNotFoundException;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import com.retrip.crew.infra.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentQuestionService implements ManageRecruitmentQuestionUseCase, GetRecruitmentQuestionUseCase {

    private final CrewQueryRepository crewQueryRepository;
    private final CrewMemberQueryRepository crewMemberQueryRepository;
    private final RecruitmentQuestionQueryRepository recruitmentQuestionQueryRepository;

    @Override
    public CreateRecruitmentQuestionResponse createRecruitmentQuestion(UUID crewId, CreateRecruitmentQuestionRequest request) {
        Crew crew = crewQueryRepository
                .findByIdWithRecruitment(crewId)
                .orElseThrow(CrewNotFoundException::new);

        RecruitmentQuestion question = RecruitmentQuestion.create(request.content(), crew);
        crew.getRecruitment().addQuestion(question);

        return CreateRecruitmentQuestionResponse.of(question);
    }

    @Override
    public UpdateRecruitmentQuestionResponse updateRecruitmentQuestion(UUID crewId, UUID questionId, UpdateRecruitmentQuestionRequest request) {
        Crew crew = crewQueryRepository
                .findByIdWithRecruitment(crewId)
                .orElseThrow(CrewNotFoundException::new);

        RecruitmentQuestion question = crew.getRecruitment()
                .getRecruitmentQuestions()
                .updateQuestion(questionId, request.content(), request.memberId());

        return UpdateRecruitmentQuestionResponse.of(question);
    }

    @Override
    public void deleteRecruitmentQuestion(UUID crewId, UUID questionId, UUID memberId) {
        Crew crew = crewQueryRepository
                .findByIdWithRecruitment(crewId)
                .orElseThrow(CrewNotFoundException::new);

        CrewMember crewMember = findCrewMemberByMemberId(crewId, memberId);

//        crew.getRecruitment()
//                .getRecruitmentQuestions()
//                .deleteQuestion(questionId, crewMember);
    }

    private CrewMember findCrewMemberByMemberId(UUID crewId, UUID memberId) {
        return crewMemberQueryRepository
                .findCrewMemberByMemberId(crewId, memberId)
                .orElseThrow(CrewMemberNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentQuestionResponse> getRecruitmentQuestions(UUID crewId) {
        return recruitmentQuestionQueryRepository.findRecruitmentQuestions(crewId);
    }
}
