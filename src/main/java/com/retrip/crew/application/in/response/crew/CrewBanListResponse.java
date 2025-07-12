package com.retrip.crew.application.in.response.crew;

import com.retrip.crew.domain.entity.CrewBanMember;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "크루 차단 리스트 조회 Response")
public record CrewBanListResponse(
        @Schema(description = "차단한 회원 리스트")
        List<BanMemberResponse> banMembers
) {
        public static CrewBanListResponse from(List<CrewBanMember> crewBanMembers) {
                return new CrewBanListResponse(BanMemberResponse.toList(crewBanMembers));
        }

        public record BanMemberResponse(
                @Schema(description = "차단한 회원 ID")
                UUID bannedMemberIds
        ){

                public static List<BanMemberResponse> toList(List<CrewBanMember> crewBanMembers) {
                        return crewBanMembers.stream()
                                .map(BanMemberResponse::from)
                                .collect(Collectors.toList());
                }

                private static BanMemberResponse from(CrewBanMember crewBanMember) {
                        return new BanMemberResponse(crewBanMember.getMemberId());
                }
        }
}
