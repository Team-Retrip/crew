package com.retrip.crew.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
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

    @Column
    private String title;

    @Version
    private long version;

    private Crew(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
    }

    public static Crew create(String title) {
        return new Crew(title);
    }
}
