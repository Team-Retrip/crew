package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;
import com.retrip.crew.application.in.response.demand.*;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageDemandUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
