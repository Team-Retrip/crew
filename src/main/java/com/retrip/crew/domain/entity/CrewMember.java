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

    @Enumerated
    private Role role;

    @Column(nullable = false)
    private UUID memberId;

    private CrewMember(Crew crew, UUID memberId, Role role) {
        this.id = UUID.randomUUID();
        this.crew = crew;
        this.memberId = memberId;
        this.role = Role.valueOf(role.name());
    }

    public static List<CrewMember> createLeader(Crew crew, UUID memberId, Role role) {
        return List.of(new CrewMember(crew, memberId, role));
    }
}
