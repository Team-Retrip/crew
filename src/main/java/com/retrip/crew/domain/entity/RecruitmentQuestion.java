package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.QuestionContent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class RecruitmentQuestion extends BaseEntity {

    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private QuestionContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_question_to_crew"))
    private Crew crew;

    private RecruitmentQuestion(String content, Crew crew) {
        this.id = UUID.randomUUID();
        this.content = new QuestionContent(content);
        this.crew = crew;
    }

    public static RecruitmentQuestion create(String content, Crew crew) {
        return new RecruitmentQuestion(content, crew);
    }

    public void update(String content, UUID memberId) {
        this.crew.validateCrewLeader(memberId);
        this.content = new QuestionContent(content);
    }
}
