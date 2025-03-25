package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.retrip.crew.domain.vo.RecruitmentStatus.RECRUITING;
import static com.retrip.crew.domain.vo.RecruitmentStatus.STOPPED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Recruitment {
    private int maxMembers;

    @Column(name = "recruitment_status")
    private RecruitmentStatus status;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Demand> demands = new ArrayList<>();

    public Recruitment(int maxMembers) {
        this.maxMembers = maxMembers;
        this.status = RECRUITING;
    }

    public void start(int membersSize) {
        if (isRecruitmentComplete(membersSize)) {
            stop();
            throw new IllegalStateException("최대 인원을 모두 모집 완료하여 더 이상 멤버를 모집할 수 없습니다.");
        }
        this.status = RECRUITING;
    }

    public void stop() {
        this.status = STOPPED;
    }

    private boolean isRecruitmentComplete(int membersSize) {
        return this.maxMembers <= membersSize;
    }

    public void updateMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Demand addDemand(UUID memberId, Crew crew) {
        if (isDuplicate(memberId)) {
            throw new IllegalStateException("이미 요청한 사용자는 다시 요청할 수 없습니다.");
        }
        Optional<Demand> reDemand = findReDemand(memberId);
        if (reDemand.isPresent()) {
            reDemand.get().restore();
            return reDemand.get();
        }
        Demand demand = new Demand(memberId, crew);
        demands.add(demand);
        return demand;
    }

    private boolean isDuplicate(UUID memberId) {
        return demands.stream()
                .anyMatch(d -> d.isEqualTo(memberId) && !d.isCanceled());
    }

    private Optional<Demand> findReDemand(UUID memberId) {
        return demands.stream()
                .filter(d -> isReDemand(memberId, d))
                .findFirst();
    }

    private static boolean isReDemand(UUID memberId, Demand demand) {
        return demand.isEqualTo(memberId)
                && demand.isCanceled();
    }

    public void cancelDemand(Demand demand) {
        Demand find = findDemand(demand);
        if (find.isNotPending()) {
            throw new IllegalStateException("참여 요청이 대기중이 아니면 취소할 수 없습니다.");
        }
        find.cancel();
    }

    public void rejectDemand(Demand demand) {
        Demand find = findDemand(demand);
        if (find.isNotPending()) {
            throw new IllegalStateException("참여 요청이 대기중이 아니면 거절할 수 없습니다.");
        }
        find.reject();
    }

    private Demand findDemand(Demand demand) {
        return demands.stream()
                .filter(d -> d.equals(demand))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("참여 요청을 찾을 수 없습니다."));
    }
}
