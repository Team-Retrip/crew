package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.QuestionUpdateFailedException;
import com.retrip.crew.domain.vo.QuestionContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
//        isUpdatable(this.getCreatedBy(), memberId);
        this.content = new QuestionContent(content);
    }

    private void isUpdatable(UUID createBy, UUID updateBy) {
        if (!createBy.equals(updateBy)) {
            throw new QuestionUpdateFailedException();
        }
    }

//    public boolean isDeletable(CrewMember crewMember) {
//        return crewMember.isLeader() || crewMember.isCreatedBy(this.getCreatedBy());
//    }
}
