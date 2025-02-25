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

    @PostMapping("/exception1")
    public ApiResponse<CrewCreateResponse> exception1(@RequestBody @Valid CrewCreateRequest request) {
        throw new CrewNotFoundException();
    }

    @PostMapping("/exception2")
    public ApiResponse<CrewCreateResponse> exception2(@RequestBody @Valid CrewCreateRequest request) {
        throw new InvalidValueException(ErrorCode.SERVER_ERROR);
    }

    @PostMapping("/exception3")
    public ApiResponse<CrewCreateResponse> exception3(@RequestBody @Valid CrewCreateRequest request) {
        throw new InvalidValueException("인풋 에러");
    }

    @PostMapping("/exception4")
    public ApiResponse<CrewCreateResponse> exception4(@RequestBody @Valid CrewCreateRequest request) {
        throw new InvalidValueException(ErrorCode.SERVER_ERROR, "서버 에러");
    }
}
