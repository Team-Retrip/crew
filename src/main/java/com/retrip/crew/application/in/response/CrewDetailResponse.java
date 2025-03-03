package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CrewDetailResponse(
        UUID id,
        String title,
        String description,
        UUID leaderId,
        Integer memberCount,
        Integer maxMemberCount,
        List<CrewMemberResponse> members
) {

    public static CrewDetailResponse of(Crew crew, int memberCount){
        return CrewDetailResponse.builder()
                .id(crew.getId())
                .title(crew.getTitle().getValue())
                .description(crew.getDescription().getValue())
                .leaderId(crew.getLeader().getMemberId())
                .memberCount(memberCount)
                .maxMemberCount(crew.getMaxMembers())
                .members(toList(crew.getCrewMembers().getValues()))
                .build();
    }

    public static List<CrewMemberResponse> toList(List<CrewMember> crewMembers){
        return crewMembers.stream()
                .map(CrewMemberResponse::from)
                .toList();
    }

    public record CrewMemberResponse(
        UUID id,
        UUID memberId,
        String roleCode,
        String roleName
    ){
        public static CrewMemberResponse from(CrewMember crewMember){
            return new CrewMemberResponse(
                    crewMember.getId(),
                    crewMember.getMemberId(),
                    crewMember.getCrewMemberRole().getCode(),
                    crewMember.getCrewMemberRole().getViewName()
            );
        }
    }
}
