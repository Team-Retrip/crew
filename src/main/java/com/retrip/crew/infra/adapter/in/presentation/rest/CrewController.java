package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.usecase.CreateCrewUseCase;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PostMapping
    public ResponseEntity<CrewCreateResponse> createCrew(@RequestBody CrewCreateRequest request) {
        CrewCreateResponse crew = createCrewUseCase.createCrew(request);
        return ResponseEntity.created(URI.create("/crews/" + crew.id())).body(crew);
    }
}
