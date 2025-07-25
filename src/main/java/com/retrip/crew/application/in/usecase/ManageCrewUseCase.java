package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewExpelRequest;
import com.retrip.crew.application.in.request.crew.CrewLeaderDelegateRequest;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.request.crew.CrewWithdrawalRequest;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewLeaderDelegateResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;

import java.util.UUID;

public interface ManageCrewUseCase {
    CrewCreateResponse createCrew(CrewCreateRequest request);

    CrewUpdateResponse updateCrew(UUID crewId, CrewUpdateRequest request);

    void withdrawCrew(UUID crewId, CrewWithdrawalRequest request);

    CrewLeaderDelegateResponse delegateCrewLeader(UUID crewId, CrewLeaderDelegateRequest request);

    void deleteCrew(UUID crewId, UUID loginMemberId);

    void expelMember(UUID crewId, CrewExpelRequest request);
}