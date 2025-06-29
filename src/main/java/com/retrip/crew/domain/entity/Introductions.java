package com.retrip.crew.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
@Embeddable
public class Introductions {

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Introduction> values = new ArrayList<>();

    public Introductions() {
        this.values = createEmptyValues();
    }

    private List<Introduction> createEmptyValues() {
        return new ArrayList<>();
    }

    public void addIntroduction(Introduction introduction) {
        this.values.add(introduction);
    }

    public void deleteIntroduction(Introduction introduction) {
        this.values.remove(introduction);
    }
}
