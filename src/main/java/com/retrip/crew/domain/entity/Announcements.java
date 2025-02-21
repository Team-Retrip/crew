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
public class Announcements {
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> values = new ArrayList<>();

    public Announcements() { //리더 생성
        this.values = createEmptyValues();
    }

    private List<Announcement> createEmptyValues() {
        return new ArrayList<>();
    }
}
