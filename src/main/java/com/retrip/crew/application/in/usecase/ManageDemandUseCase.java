package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.response.CreateDemandResponse;

import java.util.UUID;

public interface ManageDemandUseCase {
    CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request);
}
