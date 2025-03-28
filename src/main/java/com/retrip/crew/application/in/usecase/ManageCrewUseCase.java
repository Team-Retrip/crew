package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.response.CrewUpdateResponse;

import java.util.UUID;

public interface ManageCrewUseCase {
    CrewCreateResponse createCrew(CrewCreateRequest request);

    CrewUpdateResponse updateCrew(UUID crewId, CrewUpdateRequest request);


}
