package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.response.ChangeRecruitmentStatusResponse;
import com.retrip.crew.application.in.response.CrewUpdateResponse;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
@Tag(name = "Crew", description = "크루 서비스")
public class CrewController {
    private final ManageCrewUseCase manageCrewUseCase;
    private final UpdateRecruitmentUseCase updateRecruitmentUseCase;
    private ManageDemandUseCase manageDemandUseCase;
    private final GetCrewUseCase getCrewUseCase;

    @PostMapping
    @Schema(description = "크루 생성")
    public ApiResponse<CrewCreateResponse> createCrew(@RequestBody CrewCreateRequest request) {
        CrewCreateResponse crew = manageCrewUseCase.createCrew(request);
        return ApiResponse.created(crew);
    }

    @PutMapping("/{crewId}")
    @Schema(description = "크루 정보 수정")
    public ApiResponse<CrewUpdateResponse> updateCrew(
            @PathVariable UUID crewId, @RequestBody CrewUpdateRequest request) {
        CrewUpdateResponse crew = manageCrewUseCase.updateCrew(crewId, request);
        return ApiResponse.ok(crew);
    }

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

    @GetMapping
    @Schema(description = "크루 리스트 조회")
    public ResponseEntity<ApiResponse<ScrollPageResponse<CrewListResponse>>> getCrews(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "order", defaultValue = "DATE") CrewOrder order,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        ScrollPageResponse<CrewListResponse> response = getCrewUseCase.getCrews(pageable, keyword, order, sort);
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }

    @GetMapping("/{crewId}")
    @Schema(description = "크루 상세 조회")
    public ResponseEntity<ApiResponse<CrewDetailResponse>> getCrewDetail(
            @PathVariable("crewId") UUID crewId
    ) {
        CrewDetailResponse response = getCrewUseCase.getCrewDetail(crewId);
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }
}
