package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private CrewTitle title;

    @Embedded
    private CrewDescription description;

    @Embedded
    private CrewMembers crewMembers;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    @Embedded
    private Posts posts;

    @Embedded
    private Announcements announcements;

    @Embedded
    private Introductions introductions;

    @Version
    private long version;

    private Crew(String name, String description, int maxMembers, UUID leader) {
        this.id = UUID.randomUUID();
        this.title = new CrewTitle(name);
        this.description = new CrewDescription(description);
        this.maxMembers = maxMembers;
        this.crewMembers = new CrewMembers(this, leader);
        this.posts = new Posts();
        this.announcements = new Announcements();
        this.introductions = new Introductions();
    }

    public static Crew create(String title, String description, int maxMembers, UUID leader) {
        return new Crew(title, description, maxMembers, leader);
    }

    public CrewMember getLeader() {
        return crewMembers.getLeader();
    }

    public String getDescription(){
        return description.getValue();
    }

}
