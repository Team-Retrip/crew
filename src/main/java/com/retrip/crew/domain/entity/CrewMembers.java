package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
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

    public CrewMembers(Crew crew, UUID leaderId) {
        this.values.add(createLeader(crew, leaderId));
    }

    private CrewMember createLeader(Crew crew, UUID memberId) {
        return new CrewMember(crew, memberId, CrewMemberRole.LEADER);
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

    public boolean isLeader(UUID memberId) {
        return this.values.stream()
                .filter(m -> memberId.equals(m.getMemberId()))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("크루 멤버가 아닙니다."))
                .isLeader();
    }

    public void addMember(Demand demand, Crew crew) {
        if (isDuplicate(demand)) {
            throw new IllegalStateException("사용자는 이미 크루 멤버 입니다.");
        }
        CrewMember member = new CrewMember(crew, demand.getMemberId(), CrewMemberRole.PARTICIPANT);
        values.add(member);
    }

    private boolean isDuplicate(Demand demand) {
        return this.values.stream()
                .anyMatch(m -> m.getMemberId().equals(demand.getMemberId()));
    }
}
