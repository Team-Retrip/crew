package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Crew extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Version
    private long version;

    @Embedded
    private CrewTitle title;

    @Embedded
    private CrewDescription description;

    @Embedded
    private CrewMembers crewMembers;

    @Embedded
    private Posts posts;

    @Embedded
    private Announcements announcements;

    @Embedded
    private Introductions introductions;

    @Embedded
    private Recruitment recruitment;

    private Crew(String name, String description, int maxMembers, UUID leader) {
        this.id = UUID.randomUUID();
        this.title = new CrewTitle(name);
        this.description = new CrewDescription(description);
        this.recruitment = new Recruitment(maxMembers);
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

    public void startRecruitment() {
        int membersSize = crewMembers.getSize();
        this.recruitment.start(membersSize);
    }

    public void stopRecruitment() {
        this.recruitment.stop();
    }

    public void update(CrewTitle title, CrewDescription description) {
        this.title = title;
        this.description = description;
    }

    public Demand demand(UUID memberId) {
        return recruitment.addDemand(memberId, this);
    }

    public String getDescription(){
        return description.getValue();
    }
}
