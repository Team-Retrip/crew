package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.PostUpdateFailedException;
import com.retrip.crew.domain.vo.PostContent;
import com.retrip.crew.domain.vo.PostTitle;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    @Embedded
    private PostTitle title;

    @Embedded
    private PostContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crew_id",
            nullable = false,
            columnDefinition = "varbinary(16)",
            foreignKey = @ForeignKey(name = "fk_post_to_crew"))
    private Crew crew;

    private Post(String title, String content, Crew crew) {
        this.id = UUID.randomUUID();
        this.title = new PostTitle(title);
        this.content = new PostContent(content);
        this.crew = crew;
    }

    public static Post create(String title, String content, Crew crew) {
        return new Post(title, content, crew);
    }

    public void update(String title, String content, UUID memberId) {
        // isUpdatable(this.getCreatedBy(), memberId);
        this.title = new PostTitle(title);
        this.content = new PostContent(content);
    }

    private void isUpdatable(UUID createBy, UUID updateBy) {
        if (createBy != updateBy) {
            throw new PostUpdateFailedException();
        }
    }

    public boolean isDeletable(CrewMember crewMember) {
        return crewMember.isLeader();
        // Todo: 작성자만 제거 가능
        // || crewMember.isCreatedBy(this.getCreatedBy());
    }
}
