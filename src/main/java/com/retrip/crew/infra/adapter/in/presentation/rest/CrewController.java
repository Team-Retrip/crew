package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.usecase.CreateCrewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
@Tag(name = "Crew", description = "크루 서비스")
public class CrewController {
    private final CreateCrewUseCase createCrewUseCase;
    private final UpdateRecruitmentUseCase updateRecruitmentUseCase;
    private final GetCrewUseCase getCrewUseCase;

    @PostMapping
    @Schema(description = "크루 생성")
    public ApiResponse<CrewCreateResponse> createCrew(@RequestBody CrewCreateRequest request) {
        CrewCreateResponse crew = createCrewUseCase.createCrew(request);
        return ApiResponse.created(crew);
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
