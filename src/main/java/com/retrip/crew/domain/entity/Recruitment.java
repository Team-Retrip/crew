package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.DuplicateDemandException;
import com.retrip.crew.domain.exception.IllegalDemandStateException;
import com.retrip.crew.domain.exception.UnableToStartRecruitmentException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import jakarta.persistence.*;
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

    @Embedded
    private RecruitmentQuestions recruitmentQuestions;

    public Recruitment(int maxMembers, RecruitmentQuestions recruitmentQuestions) {
        this.maxMembers = maxMembers;
        this.status = RECRUITING;
        this.recruitmentQuestions = recruitmentQuestions;
    }

    public static Recruitment of(int maxMembers, List<String> questions, Crew crew) {
        RecruitmentQuestions recruitmentQuestions = new RecruitmentQuestions(questions, crew);
        return new Recruitment(maxMembers, recruitmentQuestions);
    }


    public void start(int membersSize) {
        if (isRecruitmentComplete(membersSize)) {
            stop();
            throw new UnableToStartRecruitmentException("최대 인원을 모두 모집 완료하여 더 이상 멤버를 모집할 수 없습니다.");
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
            throw new DuplicateDemandException();
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
        throwIfNotPending(find);
        find.cancel();
    }

    public void approveDemand(Demand demand) {
        Demand find = findDemand(demand);
        throwIfNotPending(find);
        find.approve();
    }

    public void rejectDemand(Demand demand) {
        Demand find = findDemand(demand);
        throwIfNotPending(find);
        find.reject();
    }

    public List<String> getRecruitmentQuestions() {
        return recruitmentQuestions.getValues().stream()
                .map(question -> question.getContent().getValue())
                .toList();
    }

    private static void throwIfNotPending(Demand find) {
        if (find.isNotPending()) {
            throw new IllegalDemandStateException("참여 요청의 상태가 대기중이 아닙니다.");
        }
    }

    private Demand findDemand(Demand demand) {
        return demands.stream()
                .filter(d -> d.equals(demand))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("참여 요청을 찾을 수 없습니다."));
    }
}
