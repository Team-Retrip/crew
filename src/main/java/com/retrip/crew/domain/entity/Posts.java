package com.retrip.crew.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Posts {
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> values = new ArrayList<>();

    public Posts() { //리더 생성
        this.values = createEmptyValues();
    }

    private List<Post> createEmptyValues() {
        return new ArrayList<>();
    }
}
