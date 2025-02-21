package com.retrip.crew.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Introductions {
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Introduction> values = new ArrayList<>();

    public Introductions() { //리더 생성
        this.values = createEmptyValues();
    }

    private List<Introduction> createEmptyValues() {
        return new ArrayList<>();
    }
}
