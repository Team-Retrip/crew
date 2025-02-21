package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.IntroductionContent;
import com.retrip.crew.domain.vo.IntroductionTitle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Introduction extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private IntroductionTitle title;

    @Embedded
    private IntroductionContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_self_introduce_board_to_crew")
    )
    private Crew crew;
}
