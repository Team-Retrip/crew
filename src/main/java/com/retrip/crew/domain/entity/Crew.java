package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.CrewContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Crew extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private CrewContent crewContent;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewMember> leader = new ArrayList<>();

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeBoard> freeBoards = new ArrayList<>();

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationBoard> notificationBoards = new ArrayList<>();

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfIntroduceBoard> selfIntroduceBoards = new ArrayList<>();

    @Version
    private long version;

    private Crew(String name, String description, UUID leader) {
        this.id = UUID.randomUUID();
        this.crewContent = new CrewContent(name, description);
        this.leader = CrewMember.createLeader(this, leader, Role.LEADER);
        this.members = new ArrayList<>();
        this.freeBoards = new ArrayList<>();
        this.notificationBoards = new ArrayList<>();
        this.selfIntroduceBoards = new ArrayList<>();
    }

    public static Crew create(String name, String description, UUID leader) {
        return new Crew(name, description, leader);
    }
}
