package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.InvalidAccessException;
import com.retrip.crew.domain.vo.IntroductionContent;
import com.retrip.crew.domain.vo.IntroductionTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Introduction extends BaseEntity {
    @Id
    @Column(columnDefinition = "varbinary(16)")
    private UUID id;

    private UUID memberId;

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

    public Introduction(UUID loginMemberId, String title, String content, Crew crew){
        this.id = UUID.randomUUID();
        this.memberId = loginMemberId;
        this.title = new IntroductionTitle(title);
        this.content = new IntroductionContent(content);
        this.crew = crew;
    }

    public static Introduction create(UUID loginMemberId, String title, String content, Crew crew) {
        return new Introduction(loginMemberId, title, content, crew);
    }

    public String getTitle(){
        return title.getValue();
    }

    public String getContent(){
        return content.getValue();
    }

    public void update(IntroductionTitle introductionTitle, IntroductionContent introductionContent, UUID loginMemberId) {
        validateIntroductionOwner(loginMemberId);
        this.title = introductionTitle;
        this.content = introductionContent;
    }

    public void validateIntroductionOwner(UUID loginMemberId){
        if(!isOwner(loginMemberId)){
            throw new InvalidAccessException();
        }
    }

    public void validateIntroductionOwnerAndLeader(UUID loginMemberId){
        if(!isOwner(loginMemberId) || !isLeader(loginMemberId)){
            throw new InvalidAccessException();
        }
    }

    public boolean isOwner(UUID loginMemberId){
        return this.memberId.equals(loginMemberId);
    }

    public boolean isLeader(UUID loginMemberId){
        return this.getCrew().getLeader().getMemberId().equals(loginMemberId);
    }

    public void delete(UUID loginMemberId, Crew crew) {
        validateIntroductionOwnerAndLeader(loginMemberId);
        crew.getIntroductions().deleteIntroduction(this);
    }
}
