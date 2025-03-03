package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.AnnouncementContent;
import com.retrip.crew.domain.vo.AnnouncementTitle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Announcement extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private AnnouncementTitle title;

    @Embedded
    private AnnouncementContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_notification_board_to_crew")
    )
    private Crew crew;
}
