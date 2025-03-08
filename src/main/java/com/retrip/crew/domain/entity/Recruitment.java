package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.retrip.crew.domain.vo.RecruitmentStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Embeddable
public class Recruitment {
    private int maxMembers;
    private RecruitmentStatus status;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();

    public Recruitment(int maxMembers) {
        this.maxMembers = maxMembers;
        this.status = RECRUITING;
    }

    public void start() {
        this.status = RECRUITING;
    }

    public void stop() {
        this.status = STOPPED;
    }
}
