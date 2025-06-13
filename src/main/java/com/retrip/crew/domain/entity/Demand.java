package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.DemandStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.retrip.crew.domain.vo.DemandStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class Demand extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;
    private UUID memberId;
    private DemandStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_demand_to_crew")
    )
    private Crew crew;

    public Demand(UUID memberId, Crew crew) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.status = PENDING;
        this.crew = crew;
    }

    public boolean isNotPending() {
        return this.status != PENDING;
    }

    public void cancel() {
        this.status = CANCELED;
    }

    public void approve() {
        this.status = APPROVED;
    }

    public void reject() {
        this.status = REJECTED;
    }

    public boolean isCanceled() {
        return this.status == CANCELED;
    }

    public boolean isEqualTo(UUID memberId) {
        return this.memberId.equals(memberId);
    }

    public void restore() {
        this.status = PENDING;
    }
}
