package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;

public interface CreateCrewUseCase {
    CrewCreateResponse createCrew(CrewCreateRequest createRequest);
}
