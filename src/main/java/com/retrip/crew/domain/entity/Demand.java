package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.IllegalDemandStateException;
import com.retrip.crew.domain.exception.common.InvalidAccessException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import com.retrip.crew.domain.vo.DemandStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.retrip.crew.domain.vo.DemandStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class Demand extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;
    private UUID memberId;
    private DemandStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_demand_to_crew")
    )
    private Crew crew;

    @OneToMany(mappedBy = "demand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentAnswer> answers = new ArrayList<>();

    public Demand(UUID memberId, Crew crew) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.status = PENDING;
        this.crew = crew;
    }

    public boolean isNotPending() {
        return this.status != PENDING;
    }

    public void cancel() {
        this.status = CANCELED;
    }

    public void approve() {
        this.status = APPROVED;
    }

    public void reject() {
        this.status = REJECTED;
    }

    public boolean isCanceled() {
        return this.status == CANCELED;
    }

    public boolean isEqualTo(UUID memberId) {
        return this.memberId.equals(memberId);
    }

    public void restore() {
        this.status = PENDING;
    }

    public void addAnswer(RecruitmentAnswer answer) {
        this.answers.add(answer);
    }

    public void updateAnswers(UUID memberId, List<RecruitmentAnswer> newAnswers) {
        if (!this.memberId.equals(memberId)) {
            throw new InvalidAccessException("자신의 참여 요청만 수정할 수 있습니다.");
        }
        if (this.status != PENDING) {
            throw new IllegalDemandStateException("대기중인 참여 요청만 수정할 수 있습니다.");
        }
        if (this.answers.size() != newAnswers.size()) {
            throw new InvalidValueException("수정하려는 답변의 개수가 올바르지 않습니다.");
        }

        Map<UUID, RecruitmentAnswer> answerMap = this.answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), Function.identity()));

        newAnswers.forEach(newAnswer -> {
            RecruitmentAnswer existingAnswer = answerMap.get(newAnswer.getQuestion().getId());
            if (existingAnswer != null) {
                existingAnswer.updateContent(newAnswer.getContent().getValue());
            }
        });
    }
}
