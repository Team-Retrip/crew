package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.CrewTrip;
import com.retrip.crew.domain.exception.CrewMemberExpelFailedException;
import com.retrip.crew.domain.exception.ImpossibleWithdrawCrewException;
import com.retrip.crew.domain.exception.NotCrewLeaderException;
import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

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
                .filter(CrewMember::isLeader)
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("크루 리더를 찾을 수 없습니다."));
    }

    public int getSize() {
        return (int) this.values.stream().filter(member -> !member.isExpelled()).count();
    }

    public boolean isLeader(UUID memberId) {
        return findMember(memberId)
                .isLeader();
    }

    public void addMember(Demand demand, Crew crew) {
        this.values.stream()
                .filter(m -> m.getMemberId().equals(demand.getMemberId()))
                .findFirst()
                .ifPresent(member -> {
                    if (member.isExpelled()) {
                        throw new IllegalStateException("추방된 사용자는 다시 크루에 참여 요청 할 수 없습니다.");
                    }
                    throw new IllegalStateException("이미 크루에 참여한 사용자입니다.");
                });

        CrewMember member = new CrewMember(crew, demand.getMemberId(), CrewMemberRole.PARTICIPANT);
        values.add(member);
    }


    public void withdraw(UUID memberId, List<CrewTrip> participatingCrewTrips) {
        validatePossibleWithdrawal(memberId, participatingCrewTrips);
        CrewMember member = findMember(memberId);
        this.values.remove(member);
    }

    private void validatePossibleWithdrawal(UUID memberId, List<CrewTrip> crewTrips) {
        if (isLeader(memberId)) {
            throw new ImpossibleWithdrawCrewException("크루 리더는 탈퇴할 수 없습니다.");
        }

        if (!CollectionUtils.isEmpty(crewTrips)) {
            validateParticipatingCrewTrips(crewTrips);
        }
    }

    private void validateParticipatingCrewTrips(List<CrewTrip> crewTrips) {
        boolean participatingExclusionCrewTrip = crewTrips.stream()
                .anyMatch(CrewTrip::isImpossibleWithdrawal);
        if (participatingExclusionCrewTrip) {
            throw new ImpossibleWithdrawCrewException("참여 중인 크루 전용 여행이 있어 탈퇴할 수 없습니다.");
        }
    }

    private CrewMember findMember(UUID memberId) {
        return this.values.stream()
                .filter(m -> memberId.equals(m.getMemberId()) && !m.isExpelled())
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("크루 멤버가 아닙니다."));
    }

    public CrewMember delegateLeader(UUID leaderId, UUID newLeaderId) {
        CrewMember leader = findMember(leaderId);
        CrewMember newLeader = findMember(newLeaderId);
        validatePossibleDelegate(leader);

        leader.changeRole(CrewMemberRole.PARTICIPANT);
        newLeader.changeRole(CrewMemberRole.LEADER);
        return newLeader;
    }

    private void validatePossibleDelegate(CrewMember leader) {
        if (!leader.isLeader()) {
            throw new NotCrewLeaderException();
        }
    }

    public void expel(UUID leaderId, UUID memberId) {
        if (!isLeader(leaderId)) {
            throw new NotCrewLeaderException();
        }

        if (leaderId.equals(memberId)) {
            throw new CrewMemberExpelFailedException("자기 자신을 추방할 수 없습니다.");
        }

        CrewMember memberToExpel = findMember(memberId);
        memberToExpel.expel();
    }
}