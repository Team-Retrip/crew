package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.response.demand.CreateDemandResponse;
import com.retrip.crew.application.in.response.demand.CrewsOfDemandResponse;
import com.retrip.crew.application.in.response.demand.DemandsResponse;
import com.retrip.crew.application.in.response.demand.RejectDemandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ManageDemandUseCase {
    CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request);

    Page<DemandsResponse> getDemands(
            UUID crewId, UUID memberId, String status, Pageable pageable, DemandOrder order, String sort);

    Page<CrewsOfDemandResponse> getCrewsOfDemand(UUID crewId, UUID demandId, UUID memberId, Pageable pageable, CrewOrder order, String sort);

    void cancelDemand(UUID crewId, UUID demandId, UUID memberId);

    RejectDemandResponse rejectDemand(UUID crewId, UUID demandId, UUID memberId);
}
