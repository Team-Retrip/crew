package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.NotCrewLeaderException;
import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE crew SET is_deleted = true WHERE id = ?")
public class Crew extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Version private long version;

    @Embedded private CrewTitle title;

    @Embedded private CrewDescription description;

    @Embedded private CrewMembers crewMembers;

    @Embedded private Posts posts;

    @Embedded private Announcements announcements;

    @Embedded private Introductions introductions;

    @Embedded private Recruitment recruitment;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    private Crew(String name, String description, int maxMembers, UUID leader, List<String> questions) {
        this.id = UUID.randomUUID();
        this.title = new CrewTitle(name);
        this.description = new CrewDescription(description);
        this.recruitment = Recruitment.of(maxMembers, questions, this);
        this.crewMembers = new CrewMembers(this, leader);
        this.posts = new Posts();
        this.announcements = new Announcements();
        this.introductions = new Introductions();
    }

    public static Crew create(String title, String description, int maxMembers, UUID leader, List<String> questions) {
        return new Crew(title, description, maxMembers, leader, questions);
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

    public void addIntroduction(Introduction introduction) {
        this.introductions.addIntroduction(introduction);
    }

    public void update(CrewTitle title, CrewDescription description) {
        this.title = title;
        this.description = description;
    }

    public Demand demand(UUID memberId) {
        return recruitment.addDemand(memberId, this);
    }

    public String getDescription() {
        return description.getValue();
    }

    public void cancelDemand(Demand demand) {
        recruitment.cancelDemand(demand);
    }

    public void rejectDemand(Demand demand) {
        recruitment.rejectDemand(demand);
    }

    public void approveDemand(Demand demand) {
        recruitment.approveDemand(demand);
        crewMembers.addMember(demand, this);
    }

    public void softDelete(UUID loginMemberId) {
        validateCrewLeader(loginMemberId);
        this.isDeleted = true;
    }

    public void validateCrewLeader(UUID loginMemberId) {
        if(!this.getCrewMembers().isLeader(loginMemberId)){
            throw new NotCrewLeaderException();
        }
    }

    public void addRecruitmentQuestion(UUID memberId, RecruitmentQuestion question) {
        validateCrewLeader(memberId);
        recruitment.addRecruitmentQuestion(question);
    }

    public void deleteRecruitmentQuestion(UUID memberId, RecruitmentQuestion savedQuestion) {
        validateCrewLeader(memberId);
        recruitment.deleteRecruitmentQuestion(savedQuestion);
    }
}
