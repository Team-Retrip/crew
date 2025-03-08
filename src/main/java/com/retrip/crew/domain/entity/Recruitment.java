package com.retrip.crew.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Recruitment {
    private int maxMembers;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();

    public Recruitment(int maxMembers) {
        this.maxMembers = maxMembers;
    }
}
