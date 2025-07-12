package com.retrip.crew.domain.entity;

import static com.retrip.crew.domain.exception.common.ErrorCode.CREW_MEMBER_NOT_IN_CREW;

import com.retrip.crew.domain.CrewTrip;
import com.retrip.crew.domain.exception.ImpossibleWithdrawCrewException;
import com.retrip.crew.domain.exception.NotCrewLeaderException;
import com.retrip.crew.domain.exception.common.BusinessException;
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
        return this.values.size();
    }

    public boolean isLeader(UUID memberId) {
        return findMember(memberId)
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
                .filter(m -> memberId.equals(m.getMemberId()))
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

    public void expelMember(UUID loginMemberId, UUID expellerId) {
        validateCrewLeader(loginMemberId);
        validateExistCrewMembers(expellerId);

        CrewMember crewMemberToExpel = values.stream()
                .filter(member -> expellerId.equals(member.getMemberId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(CREW_MEMBER_NOT_IN_CREW));

        values.remove(crewMemberToExpel);
    }

    private void validateCrewLeader(UUID loginMemberId) {
        if(!isLeader(loginMemberId)) {
            throw new NotCrewLeaderException();
        }
    }

    private void validateExistCrewMembers(UUID expellerId) {
        boolean isExist = values.stream()
                .anyMatch(member -> expellerId.equals(member.getMemberId()));

        if(!isExist) {
            throw new BusinessException(CREW_MEMBER_NOT_IN_CREW);
        }
    }
}
