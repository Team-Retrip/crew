package com.retrip.crew.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class CrewBanMember extends BaseEntity {

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

    @Column(nullable = false)
    private UUID memberId;

    public CrewBanMember(Crew crew, UUID bannedMemberId) {
        this.id = UUID.randomUUID();
        this.crew = crew;
        this.memberId = bannedMemberId;
    }
}
