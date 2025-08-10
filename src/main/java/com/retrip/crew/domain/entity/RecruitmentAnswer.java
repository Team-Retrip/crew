package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.RecruitmentAnswerContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class RecruitmentAnswer extends BaseEntity {

    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "demand_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_answer_to_demand")
    )
    private Demand demand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_answer_to_question")
    )
    private RecruitmentQuestion question;

    @Embedded
    private RecruitmentAnswerContent content;

    private RecruitmentAnswer(Demand demand, RecruitmentQuestion question, String content) {
        this.id = UUID.randomUUID();
        this.demand = demand;
        this.question = question;
        this.content = new RecruitmentAnswerContent(content);
    }

    public static RecruitmentAnswer of(Demand demand, RecruitmentQuestion question, String content) {
        return new RecruitmentAnswer(demand, question, content);
    }

    public void updateContent(String content) {
        this.content = new RecruitmentAnswerContent(content);
    }
}