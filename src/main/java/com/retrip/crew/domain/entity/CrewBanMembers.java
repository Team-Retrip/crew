package com.retrip.crew.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class CrewBanMembers {

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewBanMember> values = new ArrayList<>();

    public void banMember(Crew crew, UUID bannedMemberId) {
        this.values.add(createBanMember(crew, bannedMemberId));
    }

    private CrewBanMember createBanMember(Crew crew, UUID bannedMemberId) {
        return new CrewBanMember(crew, bannedMemberId);
    }

    public boolean isBan(UUID crewId, UUID memberId) {
        return this.values.stream()
                .anyMatch(member -> crewId.equals(member.getCrew().getId()) && memberId.equals(member.getMemberId()));
    }
}
