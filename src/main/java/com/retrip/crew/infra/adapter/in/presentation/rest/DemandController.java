package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.request.demand.UpdateDemandRequest;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.demand.*;
import com.retrip.crew.application.in.usecase.ManageDemandUseCase;
import com.retrip.crew.application.in.usecase.ManageRecruitmentQuestionUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
@Tag(name = "Recruitment", description = "크루 참여 모집 서비스")
public class DemandController {
    private final UpdateRecruitmentUseCase updateRecruitmentUseCase;
    private final ManageRecruitmentQuestionUseCase manageRecruitmentQuestionUseCase;
    private final ManageDemandUseCase manageDemandUseCase;

    @PutMapping("/{crewId}/recruitments/start")
    @Schema(description = "크루 모집 시작")
    public ApiResponse<ChangeRecruitmentStatusResponse> startRecruitment(@PathVariable final UUID crewId) {
        ChangeRecruitmentStatusResponse recruitment = updateRecruitmentUseCase.startRecruitment(crewId);
        return ApiResponse.ok(recruitment);
    }

    @PutMapping("/{crewId}/recruitments/stop")
    @Schema(description = "크루 모집 중지")
    public ApiResponse<ChangeRecruitmentStatusResponse> stopRecruitment(@PathVariable final UUID crewId) {
        ChangeRecruitmentStatusResponse recruitment = updateRecruitmentUseCase.stopRecruitment(crewId);
        return ApiResponse.ok(recruitment);
    }

    @PostMapping("/{crewId}/demands")
    @Schema(description = "크루 참여 요청")
    public ApiResponse<CreateDemandResponse> createDemand(
            @PathVariable final UUID crewId,
            @RequestBody CreateDemandRequest request) {
        CreateDemandResponse demand = manageDemandUseCase.createDemand(crewId, request);
        return ApiResponse.created(demand);
    }

    @PutMapping("/{crewId}/demands/{demandId}/cancel")
    @Schema(description = "크루 참여 요청 취소")
    public ApiResponse<Void> cancelDemand(
            @PathVariable final UUID crewId,
            @PathVariable final UUID demandId,
            @RequestParam final UUID memberId) {
        manageDemandUseCase.cancelDemand(crewId, demandId, memberId);
        return ApiResponse.noContent();
    }

    @GetMapping("/{crewId}/demands")
    @Schema(description = "크루 참여 요청 목록 조회")
    public ApiResponse<Page<DemandsResponse>> getDemands(
            @PathVariable final UUID crewId,
            @RequestParam final UUID memberId,
            @RequestParam final String status,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(name = "order", defaultValue = "DATE") DemandOrder order,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Page<DemandsResponse> demands =
                manageDemandUseCase.getDemands(crewId, memberId, status, pageable, order, sort);
        return ApiResponse.ok(demands);
    }

    @GetMapping("/{crewId}/demands/{demandId}/crews")
    @Schema(description = "크루 참여 요청자가 속한 크루 목록 조회")
    public ApiResponse<Page<CrewsOfDemandResponse>> getCrewsOfDemand(
            @PathVariable final UUID crewId,
            @PathVariable final UUID demandId,
            @RequestParam final UUID memberId,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(name = "order", defaultValue = "DATE") CrewOrder order,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {
        Page<CrewsOfDemandResponse> demands =
                manageDemandUseCase.getCrewsOfDemand(crewId, demandId, memberId, pageable, order, sort);
        return ApiResponse.ok(demands);
    }

    @PutMapping("/{crewId}/demands/{demandId}/approve")
    @Schema(description = "크루 참여 요청 승인")
    public ApiResponse<ApproveDemandResponse> approveDemand(
            @PathVariable final UUID crewId,
            @PathVariable final UUID demandId,
            @RequestParam final UUID memberId) {
        ApproveDemandResponse demand = manageDemandUseCase.approveDemand(crewId, demandId, memberId);
        return ApiResponse.ok(demand);
    }

    @PutMapping("/{crewId}/demands/{demandId}/reject")
    @Schema(description = "크루 참여 요청 거절")
    public ApiResponse<RejectDemandResponse> rejectDemand(
            @PathVariable final UUID crewId,
            @PathVariable final UUID demandId,
            @RequestParam final UUID memberId) {
        RejectDemandResponse demand = manageDemandUseCase.rejectDemand(crewId, demandId, memberId);
        return ApiResponse.ok(demand);
    }

    @PostMapping("/{crewId}/demands/question")
    @Schema(description = "크루 참여 요청 질문 등록")
    public ApiResponse<CreateRecruitmentQuestionResponse> createRecruitmentQuestion(
            @PathVariable final UUID crewId,
            @RequestParam final UUID memberId,
            @RequestBody CreateRecruitmentQuestionRequest request) {
        CreateRecruitmentQuestionResponse demand = manageRecruitmentQuestionUseCase.createRecruitmentQuestion(memberId, crewId, request);
        return ApiResponse.created(demand);
    }

    @PostMapping("/{crewId}/demands/question/{questionId}")
    @Schema(description = "크루 참여 요청 질문 수정")
    public ApiResponse<UpdateRecruitmentQuestionResponse> updateRecruitmentQuestion(
            @PathVariable final UUID crewId,
            @PathVariable final UUID questionId,
            @RequestParam final UUID memberId,
            @RequestBody UpdateRecruitmentQuestionRequest request) {
        UpdateRecruitmentQuestionResponse demand = manageRecruitmentQuestionUseCase.updateRecruitmentQuestion(memberId, crewId, questionId, request);
        return ApiResponse.ok(demand);
    }

    @DeleteMapping("/{crewId}/demands/question/{questionId}")
    @Schema(description = "크루 참여 요청 질문 삭제")
    public ApiResponse<UpdateRecruitmentQuestionResponse> deleteRecruitmentQuestion(
            @PathVariable final UUID crewId,
            @PathVariable final UUID questionId,
            @RequestParam final UUID memberId) {
        manageRecruitmentQuestionUseCase.deleteRecruitmentQuestion(memberId, crewId, questionId);
        return ApiResponse.noContent();
    }

    @GetMapping("/{crewId}/demands/question")
    @Schema(description = "크루 참여 요청 질문 조회")
    public ApiResponse<List<RecruitmentQuestionResponse>> getRecruitmentQuestions(
            @PathVariable final UUID crewId,
            @RequestParam final UUID memberId) {
        List<RecruitmentQuestionResponse> response = manageRecruitmentQuestionUseCase.getRecruitmentQuestions(crewId, memberId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/{crewId}/demands/my-demand")
    @Schema(description = "내 크루 참여 요청 조회")
    public ApiResponse<MyDemandResponse> getMyDemand(
            @PathVariable final UUID crewId,
            @RequestParam final UUID memberId) { // TODO: 로그인 구현 시 @AuthenticationPrincipal 등으로 교체
        MyDemandResponse myDemand = manageDemandUseCase.getMyDemand(crewId, memberId);
        return ApiResponse.ok(myDemand);
    }

    @PutMapping("/{crewId}/demands/my-demand")
    @Schema(description = "내 크루 참여 요청 수정")
    public ApiResponse<UpdateDemandResponse> updateMyDemand(
            @PathVariable final UUID crewId,
            @RequestBody @Valid final UpdateDemandRequest request) {
        UpdateDemandResponse response = manageDemandUseCase.updateDemand(crewId, request);
        return ApiResponse.ok(response);
    }
}
