package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.retrip.crew.domain.vo.RecruitmentStatus.RECRUITING;
import static com.retrip.crew.domain.vo.RecruitmentStatus.STOPPED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Recruitment {
    private int maxMembers;
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
        return this.maxMembers == membersSize;
    }

    public void updateMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Demand addDemand(UUID memberId, Crew crew) {
        if (isDuplicate(memberId)) {
            throw new IllegalStateException("이미 요청한 사용자는 다시 요청할 수 없습니다.");
        }
        Demand demand = new Demand(memberId, crew);
        demands.add(demand);
        return demand;
    }

    private boolean isDuplicate(UUID memberId) {
        return demands.stream()
                .map(Demand::getMemberId)
                .anyMatch(id -> id.equals(memberId));
    }
}
