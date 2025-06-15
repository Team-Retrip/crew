package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.QuestionDeleteFailedException;
import com.retrip.crew.domain.exception.QuestionNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Embeddable
public class RecruitmentQuestions {

    private static final int MAX_QUESTIONS = 10;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentQuestion> values = new ArrayList<>();

    public RecruitmentQuestions() {
        this.values = createEmptyValues();
    }

    public RecruitmentQuestions(List<String> questions, Crew crew) {
        validateQuestions(questions);
        this.values = createEmptyValues();
        questions.forEach(questionText -> {
            RecruitmentQuestion question = RecruitmentQuestion.create(questionText, crew);
            this.values.add(question);
        });
    }

    private List<RecruitmentQuestion> createEmptyValues() {
        return new ArrayList<>();
    }

    private void validateQuestions(List<String> values) {
        if (values.size() > MAX_QUESTIONS) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_COUNT);
        }
    }

    public RecruitmentQuestion updateQuestion(UUID questionId, String content, UUID memberId) {
        RecruitmentQuestion question = findQuestion(questionId);
        question.update(content, memberId);
        return question;
    }

//    public void deleteQuestion(UUID questionId, CrewMember crewMember) {
//        RecruitmentQuestion question = findQuestion(questionId);
//        if (!question.isDeletable(crewMember)) {
//            throw new QuestionDeleteFailedException();
//        }
//        this.values.remove(question);
//    }

    private RecruitmentQuestion findQuestion(UUID questionId) {
        return this.values.stream()
                .filter(q -> q.getId().equals(questionId))
                .findAny()
                .orElseThrow(QuestionNotFoundException::new);
    }

    public void add(RecruitmentQuestion question) {
        if (this.values.size() >= MAX_QUESTIONS) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_COUNT);
        }
        this.values.add(question);
    }

    public List<String> getContents() {
        return values.stream()
                .map(q -> q.getContent().getValue())
                .toList();
    }
}
