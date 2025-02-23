package com.retrip.crew.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class CrewMember extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_crew_member_to_crew")
    )
    private Crew crew;

    @Column(name = "role", length = 50, nullable = false)
    private CrewMemberRole crewMemberRole;

    @Column(nullable = false)
    private UUID memberId;

    public CrewMember(Crew crew, UUID memberId, CrewMemberRole crewMemberRole) {
        this.id = UUID.randomUUID();
        this.crew = crew;
        this.memberId = memberId;
        this.crewMemberRole = CrewMemberRole.valueOf(crewMemberRole.name());
    }
}
