package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.usecase.CreateCrewUseCase;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.infra.adapter.out.gateway.rest.member.MemberClient;
import com.retrip.crew.infra.adapter.out.gateway.rest.member.response.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService implements CreateCrewUseCase {
    private final CrewRepository crewRepository;
    private final MemberClient memberClient;

    @Override
    public CrewCreateResponse createCrew(CrewCreateRequest request) {
        MemberResponse leader = memberClient.getMember(request.leader());
        Crew entity = request.to(leader.id());
        Crew crew = crewRepository.save(entity);

        return CrewCreateResponse.of(crew);
    }
}
