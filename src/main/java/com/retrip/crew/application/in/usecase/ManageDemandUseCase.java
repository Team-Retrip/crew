package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.request.DemandOrder;
import com.retrip.crew.application.in.response.CreateDemandResponse;
import com.retrip.crew.application.in.response.DemandsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ManageDemandUseCase {
    CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request);

    Page<DemandsResponse> getDemands(
            UUID crewId, UUID memberId, String status, Pageable pageable, DemandOrder order, String sort);
}
