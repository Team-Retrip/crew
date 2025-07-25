package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.QuestionNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class RecruitmentQuestions {

    private static final int MAX_QUESTIONS = 10;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentQuestion> values = new ArrayList<>();

    public RecruitmentQuestions(List<String> questions, Crew crew) {
        validateQuestions(questions);
        questions.forEach(question -> this.values.add(RecruitmentQuestion.create(question, crew)));
    }

    private void validateQuestions(List<String> values) {
        if (values.size() > MAX_QUESTIONS) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_COUNT);
        }
    }

    public void add(RecruitmentQuestion question) {
        if (this.values.size() >= MAX_QUESTIONS) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_COUNT);
        }
        this.values.add(question);
    }

    public void delete(RecruitmentQuestion savedQuestion) {
        this.values.remove(savedQuestion);
    }
}
