package com.retrip.crew.domain.entity;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class RecruitmentQuestions {

    private static final int MAX_QUESTIONS = 10;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentQuestion> values = new ArrayList<>();

    public RecruitmentQuestions(List<String> values, Crew crew) {
        validateQuestions(values);
        this.values = values.stream()
                .map(questionText -> RecruitmentQuestion.create(questionText, crew))
                .toList();
    }

    private void validateQuestions(List<String> values) {
        if (values.size() > MAX_QUESTIONS) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_COUNT);
        }
    }

    public List<String> getContents() {
        return values.stream()
                .map(q -> q.getContent().getValue())
                .toList();
    }
}
