package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.IntroductionCreateRequest;
import com.retrip.crew.application.in.request.IntroductionDeleteRequest;
import com.retrip.crew.application.in.request.IntroductionUpdateRequest;
import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.request.crew.CrewWithdrawalRequest;
import com.retrip.crew.application.in.response.IntroductionCreateResponse;
import com.retrip.crew.application.in.response.IntroductionDetailResponse;
import com.retrip.crew.application.in.response.IntroductionListResponse;
import com.retrip.crew.application.in.response.IntroductionUpdateResponse;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageIntroductionUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
@Tag(name = "Crew", description = "크루 서비스")
public class CrewController {
    private final ManageCrewUseCase manageCrewUseCase;
    private final GetCrewUseCase getCrewUseCase;
    private final ManageIntroductionUseCase manageIntroductionUseCase;

    @Schema(description = "크루 생성")
    @PostMapping
    public ApiResponse<CrewCreateResponse> createCrew(@RequestBody CrewCreateRequest request) {
        CrewCreateResponse crew = manageCrewUseCase.createCrew(request);
        return ApiResponse.created(crew);
    }

    @Schema(description = "크루 정보 수정")
    @PutMapping("/{crewId}")
    public ApiResponse<CrewUpdateResponse> updateCrew(
            @PathVariable UUID crewId, @RequestBody CrewUpdateRequest request) {
        CrewUpdateResponse crew = manageCrewUseCase.updateCrew(crewId, request);
        return ApiResponse.ok(crew);
    }

    @Schema(description = "크루 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<ScrollPageResponse<CrewListResponse>>> getCrews(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "order", defaultValue = "DATE") CrewOrder order,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        ScrollPageResponse<CrewListResponse> response = getCrewUseCase.getCrews(pageable, keyword, order, sort);
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }

    @Schema(description = "크루 상세 조회")
    @GetMapping("/{crewId}")
    public ResponseEntity<ApiResponse<CrewDetailResponse>> getCrewDetail(
            @PathVariable("crewId") UUID crewId
    ) {
        CrewDetailResponse response = getCrewUseCase.getCrewDetail(crewId);
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }

    @Schema(description = "크루 자기소개 등록")
    @PostMapping("/{crewId}/introductions")
    public ApiResponse<IntroductionCreateResponse> createIntroduction(
            @PathVariable final UUID crewId,
            @RequestBody IntroductionCreateRequest request) {
        IntroductionCreateResponse response = manageIntroductionUseCase.createIntroduction(crewId, request);
        return ApiResponse.created(response);
    }

    @Schema(description = "크루 자기소개 수정")
    @PutMapping("/{crewId}/introductions/{introductionId}")
    public ApiResponse<IntroductionUpdateResponse> updateIntroduction(
            @PathVariable("crewId") final UUID crewId,
            @PathVariable("introductionId") final UUID introductionId,
            @RequestBody IntroductionUpdateRequest request) {
        IntroductionUpdateResponse response = manageIntroductionUseCase.updateIntroduction(crewId, introductionId, request);
        return ApiResponse.created(response);
    }

    @Schema(description = "크루 자기소개 삭제")
    @DeleteMapping("/{crewId}/introductions/{introductionId}")
    public ApiResponse<IntroductionCreateResponse> deleteIntroduction(
            @PathVariable("crewId") final UUID crewId,
            @PathVariable("introductionId") final UUID introductionId,
            @RequestBody IntroductionDeleteRequest request) {
        manageIntroductionUseCase.deleteIntroduction(crewId, introductionId, request);
        return ApiResponse.created(null);
    }

    @Schema(description = "크루 자기소개 상세 조회")
    @GetMapping("/{crewId}/introductions/{introductionId}")
    public ApiResponse<IntroductionDetailResponse> getIntroduction(
            @PathVariable("crewId") final UUID crewId,
            @PathVariable("introductionId") final UUID introductionId
    ) {
        IntroductionDetailResponse response = manageIntroductionUseCase.getIntroduction(crewId, introductionId);
        return ApiResponse.ok(response);
    }

    @Schema(description = "크루 자기소개 리스트 조회")
    @GetMapping("/{crewId}/introductions")
    public ApiResponse<ScrollPageResponse<IntroductionListResponse>> getIntroductions(
            @PathVariable("crewId") final UUID crewId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        ScrollPageResponse<IntroductionListResponse> response = manageIntroductionUseCase.getIntroductions(crewId, pageable);
        return ApiResponse.ok(response);
    }

    @Schema(description = "크루 탈퇴")
    @PutMapping("/{crewId}/withdrawal")
    public ApiResponse<Void> withdrawCrew(
            @PathVariable final UUID crewId,
            @RequestBody CrewWithdrawalRequest request) {
        manageCrewUseCase.withdrawCrew(crewId, request);
        return ApiResponse.noContent();
    }
}
