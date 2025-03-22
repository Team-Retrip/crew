package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.request.DemandOrder;
import com.retrip.crew.application.in.response.CreateDemandResponse;
import com.retrip.crew.application.in.response.PendingDemandsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ManageDemandUseCase {
    CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request);

    Page<PendingDemandsResponse> getPendingDemands(
            UUID crewId, UUID memberId, Pageable pageable, DemandOrder order, String sort);
}
