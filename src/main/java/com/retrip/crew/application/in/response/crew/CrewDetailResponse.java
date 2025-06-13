package com.retrip.crew.application.in.response.crew;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Schema
@Builder
public record CrewDetailResponse(
        @Schema(description = "크루 ID")
        UUID id,

        @Schema(description = "크루 타이틀")
        String title,

        @Schema(description = "크루 설명")
        String description,

        @Schema(description = "크루 리더 ID")
        UUID leaderId,

        @Schema(description = "크루원 현재 인원수")
        Integer memberCount,

        @Schema(description = "크루원 수용가능한 최대 인원수")
        Integer maxMemberCount,

        @Schema(description = "크루원 리스트")
        List<CrewMemberResponse> members
) {

    public static CrewDetailResponse of(Crew crew, int memberCount){
        return CrewDetailResponse.builder()
                .id(crew.getId())
                .title(crew.getTitle().getValue())
                .description(crew.getDescription())
                .leaderId(crew.getLeader().getMemberId())
                .memberCount(memberCount)
                .maxMemberCount(crew.getRecruitment().getMaxMembers())
                .members(toList(crew.getCrewMembers().getValues()))
                .build();
    }

    public static List<CrewMemberResponse> toList(List<CrewMember> crewMembers){
        return crewMembers.stream()
                .map(CrewMemberResponse::from)
                .toList();
    }

    @Schema
    public record CrewMemberResponse(

        @Schema(description = "크루 멤버 고유 ID")
        UUID id,

        @Schema(description = "크루원 ID")
        UUID memberId,

        @Schema(description = "크루원 역할코드")
        String roleCode,

        @Schema(description = "크루원 역할명")
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
