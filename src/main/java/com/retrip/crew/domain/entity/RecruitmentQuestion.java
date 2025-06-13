package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.QuestionContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class RecruitmentQuestion {

    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private QuestionContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false, columnDefinition = "varbinary(16)", foreignKey = @ForeignKey(name = "fk_question_to_crew"))
    private Crew crew;

    public RecruitmentQuestion(UUID id, String content, Crew crew) {
        this.id = UUID.randomUUID();
        this.content = new QuestionContent(content);
        this.crew = crew;
    }

    public static RecruitmentQuestion create(String content, Crew crew) {
        return new RecruitmentQuestion(UUID.randomUUID(), content, crew);
    }
}
