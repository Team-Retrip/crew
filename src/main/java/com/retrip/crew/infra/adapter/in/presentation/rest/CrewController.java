package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.response.CrewDetailResponse;
import com.retrip.crew.application.in.response.CrewListResponse;
import com.retrip.crew.application.in.usecase.CreateCrewUseCase;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
public class CrewController {
    private final CreateCrewUseCase createCrewUseCase;
    private final GetCrewUseCase getCrewUseCase;

    @PostMapping
    public ResponseEntity<CrewCreateResponse> createCrew(@RequestBody CrewCreateRequest request) {
        CrewCreateResponse crew = createCrewUseCase.createCrew(request);
        return ResponseEntity.created(URI.create("/crews/" + crew.id())).body(crew);
    }

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

    @GetMapping("/{crewId}")
    public ResponseEntity<ApiResponse<CrewDetailResponse>> getCrewDetail(
            @PathVariable("crewId") UUID crewId
    ) {
        CrewDetailResponse response = getCrewUseCase.getCrewDetail(crewId);
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }
}
