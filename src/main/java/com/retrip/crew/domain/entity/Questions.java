package com.retrip.crew.domain.entity;

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
public class Questions {

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> values = new ArrayList<>();

    public Questions(List<String> values, Crew crew) {
        valid(values);
        this.values = values.stream().map(s -> Question.create(s, crew)).toList();
    }

    private void valid(List<String> values) {
        // 질문의 개수 체크 (최소 1개, 최대 10개)
        if (values.size() > 10) {
            throw new IllegalArgumentException("질문은 10개 이하로 입력해야 합니다.");
        }

        // 각 질문의 길이 체크 (최소 1자 이상, 최대 200자 이하)
        for (String question : values) {
            if (question.length() < 1 || question.length() > 200) {
                throw new IllegalArgumentException("각 질문은 최소 50자 이상, 200자 이하로 입력해야 합니다.");
            }
        }
    }

}
