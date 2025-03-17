package com.retrip.crew.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class CrewMembers {
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewMember> values = new ArrayList<>();

    public CrewMembers(Crew crew, UUID member) {
        this.values = createLeader(crew, member);
    }

    private List<CrewMember> createLeader(Crew crew, UUID memberId) {
        return List.of(new CrewMember(crew, memberId, CrewMemberRole.LEADER));
    }

    public CrewMember getLeader() {
        return this.values.stream()
                .filter(it -> it.getCrewMemberRole() == CrewMemberRole.LEADER)
                .findFirst()
                .orElse(null);
    }

    public int getSize() {
        return this.values.size();
    }
}
